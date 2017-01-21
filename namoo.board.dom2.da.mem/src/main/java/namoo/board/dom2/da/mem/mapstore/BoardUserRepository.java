/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:hkkang@nextree.co.kr">Kang Hyoungkoo</a>
 * @since 2015. 1. 15.
 */
package namoo.board.dom2.da.mem.mapstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import namoo.board.dom2.da.mem.util.ObjectCopyUtil;
import namoo.board.dom2.entity.user.BoardUser;

public class BoardUserRepository {
    //
    private static BoardUserRepository instance = new BoardUserRepository();
    private Map<String, BoardUser> userMap = new HashMap<String, BoardUser>();
    
    
    private BoardUserRepository() {}
    
    public static BoardUserRepository getInstance() {
        //
        return instance;
    }
    
    //--------------------------------------------------------------------------
    
    public void put(BoardUser user) {
        //
        BoardUser copied = ObjectCopyUtil.copyObject(user, BoardUser.class);
        this.userMap.put(user.getEmail(), copied);
    }
    
    public BoardUser get(String email) {
        //
        BoardUser user = this.userMap.get(email);
        return ObjectCopyUtil.copyObject(user, BoardUser.class);
    }
    
    public List<BoardUser> getByName(String name) {
        //
        List<BoardUser> users = new ArrayList<BoardUser>();
        
        for (BoardUser user : this.userMap.values()) {
            if (user.getName().equals(name)) {
                users.add(user);
            }
        }
        
        return ObjectCopyUtil.copyObjects(users, BoardUser.class);
    }
    
    public List<BoardUser> getAll() {
        //
        return ObjectCopyUtil.copyObjects(this.userMap.values(), BoardUser.class);
    }
    
    public void remove(String email) {
        //
        this.userMap.remove(email);
    }
}
