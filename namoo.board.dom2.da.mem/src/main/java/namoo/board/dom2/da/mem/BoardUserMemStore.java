/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:hkkang@nextree.co.kr">Kang Hyoungkoo</a>
 * @since 2015. 1. 15.
 */
package namoo.board.dom2.da.mem;

import java.util.List;

import namoo.board.dom2.da.mem.mapstore.BoardUserRepository;
import namoo.board.dom2.entity.user.BoardUser;
import namoo.board.dom2.store.BoardUserStore;
import namoo.board.dom2.util.exception.EmptyResultException;

public class BoardUserMemStore implements BoardUserStore {
    //
    private BoardUserRepository userStore = BoardUserRepository.getInstance();
    
    //--------------------------------------------------------------------------
    
    @Override
    public void create(BoardUser user) {
        // 
        userStore.put(user);
    }

    @Override
    public BoardUser retrieveByEmail(String email) throws EmptyResultException {
        // 
        BoardUser user = userStore.get(email); 
        if (user == null) {
            throw new EmptyResultException("No such a user --> " + email);
        }
        
        return user;
    }

    @Override
    public List<BoardUser> retrieveAll() {
        // 
        return userStore.getAll();
    }

    @Override
    public List<BoardUser> retrieveByName(String name) {
        // 
        return userStore.getByName(name);
    }

    @Override
    public void update(BoardUser user) {
        // 
        userStore.put(user);
    }

    @Override
    public void deleteByEmail(String email) {
        // 
        userStore.remove(email);
    }

    @Override
    public boolean isExistByEmail(String email) {
        // 
        BoardUser user = userStore.get(email);
        return user != null;
    }

}
