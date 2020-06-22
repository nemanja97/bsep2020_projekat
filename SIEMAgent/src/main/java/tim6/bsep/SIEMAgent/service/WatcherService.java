package tim6.bsep.SIEMAgent.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinNT;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tim6.bsep.SIEMAgent.configuration.AgentConfigItem;
import tim6.bsep.SIEMAgent.model.Log;
import tim6.bsep.SIEMAgent.utility.SignatureUtility;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Null;
import java.io.*;
import java.nio.file.*;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static tim6.bsep.SIEMAgent.utility.EventLogRecordUtility.getDescription;


@Service
public class WatcherService {

    private HashMap<String, Long> index = new HashMap<>();
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.keystore-name}")
    private String keyStoreName;
    @Value("${app.keystore-pass}")
    private String password;
    @Value("${app.alias}")
    private String serverCertificateAlias;

    public Runnable watch(AgentConfigItem item) {
        return () -> {
            try {
                String directoryPath = item.getPath();
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = Paths.get(directoryPath);
                path.register(
                        watchService,
                        StandardWatchEventKinds.ENTRY_MODIFY);
                WatchKey key;

                ArrayList<String> linesToAdd;

                while ((key = watchService.take()) != null) {
                    if(item.getBatch()){
                        Thread.sleep(item.getBatchTime());
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        String filePath = directoryPath + File.separator + event.context();

                        if(filePath.contains(".idx")){
                            continue;
                        }

                        Long fileStart = get(filePath);
                        linesToAdd = readLines(filePath, fileStart);
                        if(linesToAdd.size() > 0){
                            handleLines(linesToAdd, item);
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        };
    }

    public Runnable watchWindows(AgentConfigItem item) {
        return () -> {
            try {
                String logName = item.getPath();
                while (true){
                    Advapi32Util.EventLogIterator iterator = new Advapi32Util.EventLogIterator(null, logName, WinNT.EVENTLOG_BACKWARDS_READ);
                    ArrayList<Advapi32Util.EventLogRecord> recordsToAdd = new ArrayList<>();
                    while (iterator.hasNext()) {
                        Advapi32Util.EventLogRecord curRecord = iterator.next();
                        if(curRecord.getRecordNumber() <= get(logName)){
                            iterator.close();
                            break;
                        }
                        recordsToAdd.add(curRecord);
                    }
                    if(recordsToAdd.size() > 0){
                        handleRecords(recordsToAdd, item);
                        put(logName, (long) recordsToAdd.get(0).getRecordNumber());
                    }
                    if(item.getBatch()){
                        Thread.sleep(item.getBatchTime());
                    }else{
                        Thread.sleep(500);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        };
    }

    private void handleLines(ArrayList<String> lines, AgentConfigItem item) {
        ArrayList<Log> logs = new ArrayList<>();
        for (String line: lines) {
            if(checkRegex(line, item)){
                logs.add(new Log(line, item.getSimulated()));
            }
        }
        if(logs.size() > 0){
            performClientRequest(logs);
        }
    }

    private void handleRecords(ArrayList<Advapi32Util.EventLogRecord> records, AgentConfigItem item) {
        ArrayList<Log> logs = new ArrayList<>();
        for (Advapi32Util.EventLogRecord record: records) {
            if(checkRegex(getDescription(record), item)){
                logs.add(new Log(record));
            }
        }
        if(logs.size() > 0){
            performClientRequest(logs);
        }
    }

    private void performClientRequest(ArrayList<Log> logs) {
        try{
            String msg = mapper.writeValueAsString(logs);
            System.out.println(msg);
            byte[] signedData = SignatureUtility.signMessage(msg, keyStoreName, password, serverCertificateAlias);
            restTemplate.postForEntity("https://localhost:8044/api/v1/test", signedData, String.class);
        } catch (IOException | CertificateEncodingException | CMSException | OperatorCreationException e){
            e.printStackTrace();
        } catch (RestClientException ignored){

        }
    }

    private boolean checkRegex(String line, AgentConfigItem item){
        for (String regex: item.getRegexList()) {
            if(!line.matches(regex)){
                return false;
            }
        }
        return true;
    }

    private ArrayList<String> readLines(String path, Long start) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(path, "r");
        raf.seek(start);
        ArrayList<String> lines = new ArrayList<>();
        String line;
        while ((line = raf.readLine()) != null) {
            if(line.isBlank()){
                continue;
            }
            lines.add(line);
        }
        put(path, raf.getFilePointer());
        raf.close();
        return lines;
    }

    public Long get(String key) {
        Long val = this.index.get(key);
        if(val == null){
            val = 0L;
        }
        return val;
    }

    public synchronized void put(String key, Long val) {
        index.put(key, val);
        save();
    }


    private void save(){
        try {
            FileOutputStream fileOut = new FileOutputStream(new File("./index.idx"));
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.index);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    @PostConstruct
    public void load(){
        try {
            FileInputStream fileIn = new FileInputStream(new File("./index.idx"));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.index = (HashMap<String, Long>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            this.index = new HashMap<>();
        } catch (ClassNotFoundException ignored) {
        }
    }
}
