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
import namoo.board.dom2.entity.board.Posting;

public class PostingRepository {
    //
    private static PostingRepository instance = new PostingRepository();
    private Map<String, Posting> postingMap = new HashMap<String, Posting>();
    
    
    private PostingRepository() {}
    
    public static PostingRepository getInstance() {
        //
        return instance;
    }
    
    //--------------------------------------------------------------------------
    
    public void put(Posting posting) {
        //
        Posting copied = ObjectCopyUtil.copyObject(posting, Posting.class);
        this.postingMap.put(posting.getUsid(), copied);
    }
    
    public Posting get(String usid) {
        //
        Posting user = this.postingMap.get(usid);
        return ObjectCopyUtil.copyObject(user, Posting.class);
    }
    
    public List<Posting> getByBoard(String boardUsid) {
        //
        List<Posting> postings = new ArrayList<Posting>();
        
        for (Posting posting : this.postingMap.values()) {
            if (posting.getBoardUsid().equals(boardUsid)) {
                postings.add(ObjectCopyUtil.copyObject(posting, Posting.class));
            }
        }
        return ObjectCopyUtil.copyObjects(postings, Posting.class);
    }
    
    public List<Posting> getAll() {
        //
        return ObjectCopyUtil.copyObjects(this.postingMap.values(), Posting.class);
    }
    
    public void update(Posting posting) {
        //
        Posting saved = postingMap.get(posting.getUsid());
        if (saved == null) {
            return;
        }
        
        Posting copied = ObjectCopyUtil.copyObject(posting, Posting.class);
        postingMap.put(copied.getUsid(), copied);
    }
    
    public void remove(String usid) {
        //
        this.postingMap.remove(usid);
    }
    
    public void removeByBoard(String boardUsid) {
        //
        List<String> removeUsids = new ArrayList<String>();
        
        for (Posting posting : postingMap.values()) {
            //
            if (posting.getBoardUsid().equals(boardUsid)) {
                removeUsids.add(posting.getUsid());
            }
        }
        for (String usid : removeUsids) {
            postingMap.remove(usid);
        }
    }
}
