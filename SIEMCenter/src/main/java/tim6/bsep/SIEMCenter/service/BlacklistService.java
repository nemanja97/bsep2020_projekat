package tim6.bsep.SIEMCenter.service;

import tim6.bsep.SIEMCenter.model.Blacklist;

import java.util.List;

public interface BlacklistService {

    List<Blacklist> getAll();

    Blacklist findById(Long id);

    Blacklist getByName(String name);

    void create(Blacklist blacklist);

    boolean update(Long id, Blacklist blacklist);

    boolean delete(Long id);
}
