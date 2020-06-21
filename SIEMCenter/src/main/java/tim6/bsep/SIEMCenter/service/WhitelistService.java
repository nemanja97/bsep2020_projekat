package tim6.bsep.SIEMCenter.service;

import tim6.bsep.SIEMCenter.model.Whitelist;

import java.util.List;

public interface WhitelistService {

    List<Whitelist> getAll();

    Whitelist findById(Long id);

    Whitelist getByName(String name);

    void create(Whitelist whitelist);

    boolean update(Long id, Whitelist whitelist);

    boolean delete(Long id);
}
