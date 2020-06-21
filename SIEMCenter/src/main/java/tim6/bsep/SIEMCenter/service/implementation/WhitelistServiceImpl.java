package tim6.bsep.SIEMCenter.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.SIEMCenter.model.Whitelist;
import tim6.bsep.SIEMCenter.repository.WhitelistRepository;
import tim6.bsep.SIEMCenter.service.WhitelistService;

import java.util.List;

@Service
public class WhitelistServiceImpl implements WhitelistService {

    @Autowired
    WhitelistRepository whitelistRepository;

    @Autowired
    private NextSequenceService nextSequenceService;

    @Override
    public List<Whitelist> getAll() {
        return whitelistRepository.findAll();
    }

    @Override
    public Whitelist findById(Long id) {
        return whitelistRepository.findById(id).orElse(null);
    }

    @Override
    public Whitelist getByName(String name) {
        return whitelistRepository.findByName(name);
    }

    @Override
    public void create(Whitelist whitelist) {
        whitelist.setId(nextSequenceService.whiteListGetNextSequence());
        whitelistRepository.save(whitelist);
    }

    @Override
    public boolean update(Long id, Whitelist whitelist) {
        Whitelist oldWhitelist = findById(id);
        if (oldWhitelist != null) {
            oldWhitelist.setContent(whitelist.getContent());
            oldWhitelist.setName(oldWhitelist.getName());
            whitelistRepository.save(oldWhitelist);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        Whitelist whitelist = findById(id);
        if (whitelist != null) {
            whitelistRepository.delete(whitelist);
            return true;
        }
        return false;
    }
}
