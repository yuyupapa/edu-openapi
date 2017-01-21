/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:hkkang@nextree.co.kr">Kang Hyoungkoo</a>
 * @since 2015. 1. 15.
 */
package namoo.board.dom2.da.mem.mapstore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import namoo.board.dom2.da.mem.util.ObjectCopyUtil;
import namoo.board.dom2.entity.board.SocialBoard;

public class SocialBoardRepository {
    //
    private static SocialBoardRepository instance = new SocialBoardRepository();
    private Map<String, SocialBoard> boardMap = new HashMap<String, SocialBoard>();
    
    
    private SocialBoardRepository() {}
    
    public static SocialBoardRepository getInstance() {
        //
        return instance;
    }
    
    //--------------------------------------------------------------------------
    
    public void put(SocialBoard board) {
        //
        SocialBoard copied = ObjectCopyUtil.copyObject(board, SocialBoard.class);
        this.boardMap.put(board.getUsid(), copied);
    }
    
    public SocialBoard get(String usid) {
        //
        SocialBoard board = this.boardMap.get(usid);
        return ObjectCopyUtil.copyObject(board, SocialBoard.class);
    }
    
    public SocialBoard getByName(String name) {
        //
        for (SocialBoard board : this.boardMap.values()) {
            if (board.getName().equals(name)) {
                return ObjectCopyUtil.copyObject(board, SocialBoard.class);
            }
        }
        return null;
    }
    
    public List<SocialBoard> getAll() {
        //
        return ObjectCopyUtil.copyObjects(this.boardMap.values(), SocialBoard.class);
    }
    
    public void update(SocialBoard board) {
        //
        SocialBoard saved = boardMap.get(board.getUsid());
        if (saved == null) {
            return;
        }
        
        SocialBoard copied = ObjectCopyUtil.copyObject(board, SocialBoard.class);
        boardMap.put(copied.getUsid(), copied);
    }
    
    public void remove(String usid) {
        //
        this.boardMap.remove(usid);
    }
}
