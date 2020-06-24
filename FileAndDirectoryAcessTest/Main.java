package tim6.bsep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.nio.file.attribute.AclEntryFlag.DIRECTORY_INHERIT;
import static java.nio.file.attribute.AclEntryFlag.FILE_INHERIT;
import static java.nio.file.attribute.AclEntryPermission.*;

public class Main {

    public static void main(String[] args) throws IOException {

        String filePath = "E:\\logs3";

        File file = new File(filePath);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory created!");
            }
        }

        if (System.getProperty("os.name").contains("Windows")) {
            // Windows ACL

            Path path = Paths.get(filePath);
            GroupPrincipal administrators = path.getFileSystem().getUserPrincipalLookupService()
                    .lookupPrincipalByGroupName("Administrators");
            AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);
            AclEntry adminEntry = AclEntry.newBuilder()
                    .setType(AclEntryType.ALLOW)
                    .setPrincipal(administrators)
                    .setFlags(DIRECTORY_INHERIT,
                            FILE_INHERIT)
                    .setPermissions(SYNCHRONIZE,
                            READ_DATA,
                            EXECUTE,
                            APPEND_DATA,
                            READ_ATTRIBUTES,
                            READ_NAMED_ATTRS,
                            READ_ACL,
                            WRITE_ATTRIBUTES,
                            WRITE_OWNER,
                            WRITE_NAMED_ATTRS,
                            READ_NAMED_ATTRS,
                            WRITE_DATA)
                    .build();

            GroupPrincipal system = path.getFileSystem().getUserPrincipalLookupService()
                    .lookupPrincipalByGroupName("SYSTEM");
            view = Files.getFileAttributeView(path, AclFileAttributeView.class);
            AclEntry sysEntry = AclEntry.newBuilder()
                    .setType(AclEntryType.ALLOW)
                    .setPrincipal(system)
                    .setFlags(DIRECTORY_INHERIT,
                            FILE_INHERIT)
                    .setPermissions(
                            DELETE_CHILD,
                            SYNCHRONIZE,
                            DELETE,
                            READ_DATA,
                            EXECUTE,
                            APPEND_DATA,
                            READ_ATTRIBUTES,
                            READ_NAMED_ATTRS,
                            READ_ACL,
                            WRITE_ATTRIBUTES,
                            WRITE_ACL,
                            WRITE_OWNER,
                            WRITE_NAMED_ATTRS,
                            READ_NAMED_ATTRS,
                            WRITE_DATA)
                    .build();

            List<AclEntry> aclEntries = new ArrayList<>();
            aclEntries.add(adminEntry);
            aclEntries.add(sysEntry);
            view.setAcl(aclEntries);
        } else {
            // Unix ACL
            try {
                Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwx---");
                Files.setPosixFilePermissions(file.toPath(), permissions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
