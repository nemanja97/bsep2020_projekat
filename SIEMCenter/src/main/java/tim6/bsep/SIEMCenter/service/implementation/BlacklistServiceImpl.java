package tim6.bsep.SIEMCenter.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.SIEMCenter.model.Blacklist;
import tim6.bsep.SIEMCenter.repository.BlacklistRepository;
import tim6.bsep.SIEMCenter.service.BlacklistService;

import java.util.List;

@Service
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    BlacklistRepository blacklistRepository;

    @Autowired
    private NextSequenceService nextSequenceService;

    @Override
    public List<Blacklist> getAll() {
        return blacklistRepository.findAll();
    }

    @Override
    public Blacklist findById(Long id) {
        return blacklistRepository.findById(id).orElse(null);
    }

    @Override
    public Blacklist getByName(String name) {
        return blacklistRepository.findByName(name);
    }

    @Override
    public void create(Blacklist blacklist) {
        blacklist.setId(nextSequenceService.blackListGetNextSequence());
        blacklistRepository.save(blacklist);
    }

    @Override
    public boolean update(Long id, Blacklist blacklist) {
        Blacklist oldBlacklist = findById(id);
        if (oldBlacklist != null) {
            oldBlacklist.setContent(blacklist.getContent());
            oldBlacklist.setName(blacklist.getName());
            blacklistRepository.save(oldBlacklist);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        Blacklist blacklist = findById(id);
        if (blacklist != null) {
            blacklistRepository.delete(blacklist);
            return true;
        }
        return false;
    }

}
