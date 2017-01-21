/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:hkkang@nextree.co.kr">Kang Hyoungkoo</a>
 * @since 2015. 1. 15.
 */
package namoo.board.dom2.da.mem;

import java.util.ArrayList;
import java.util.List;

import namoo.board.dom2.da.mem.mapstore.PostingRepository;
import namoo.board.dom2.da.mem.mapstore.UsidSeqRepository;
import namoo.board.dom2.entity.board.Posting;
import namoo.board.dom2.entity.board.PostingComment;
import namoo.board.dom2.entity.board.PostingContents;
import namoo.board.dom2.store.PostingStore;
import namoo.board.dom2.util.exception.EmptyResultException;
import namoo.board.dom2.util.page.Page;
import namoo.board.dom2.util.page.PageCriteria;

public class PostingMemStore implements PostingStore {
    //
    private PostingRepository postingRepository = PostingRepository.getInstance();
    private UsidSeqRepository seqStore = UsidSeqRepository.getInstance();
    
    //--------------------------------------------------------------------------
    // posting
    
    @Override
    public void create(Posting posting) {
        // 
        postingRepository.put(posting);
    }

    @Override
    public Posting retrieve(String usid) throws EmptyResultException {
        // 
        Posting posting = postingRepository.get(usid); 
        if (posting == null) {
            throw new EmptyResultException("No such a posting --> " + usid);
        }
        return posting;
    }

    @Override
    public Page<Posting> retrievePage(String boardUsid, PageCriteria pageCriteria) {
        // 
        List<Posting> allResults = postingRepository.getByBoard(boardUsid);
        
        int totalSize = allResults.size();
        pageCriteria.setTotalItemCount(totalSize);

        List<Posting> pageResults = new ArrayList<Posting>();
        int start = pageCriteria.getStart();
        int itemPageCount = pageCriteria.getItemLimitOfPage();
        
        for (int i = start; i < (start + itemPageCount); i ++) {
            //
            if (i >= totalSize) {
                break;
            }
            pageResults.add(allResults.get(i));
        }
        
        return new Page<Posting>(pageResults, pageCriteria);
    }

    @Override
    public void update(Posting posting) {
        // 
        postingRepository.update(posting);
    }

    @Override
    public void delete(String usid) {
        // 
        postingRepository.remove(usid);
    }

    @Override
    public void deleteByBoard(String boardUsid) {
        // 
        postingRepository.removeByBoard(boardUsid);
    }

    @Override
    public int nextSequence(String boardUsid) {
        // 
        return seqStore.nextPostingSequence(boardUsid);
    }

    @Override
    public void increaseReadCount(String usid) {
        // 
        Posting posting = postingRepository.get(usid);
        posting.setReadCount(posting.getReadCount() + 1);
        
        postingRepository.update(posting);
    }
    
    @Override
    public boolean isExist(String usid) {
        // 
        return postingRepository.get(usid) != null;
    }

    @Override
    public void createContents(PostingContents contents) {
        // 
        Posting posting = postingRepository.get(contents.getPostingUsid());
        posting.setContents(contents);
        
        postingRepository.put(posting);
    }

    @Override
    public PostingContents retrieveContents(String postingUsid) throws EmptyResultException {
        // 
        Posting posting = postingRepository.get(postingUsid);
        if (posting == null) {
            throw new EmptyResultException("No such a postingConents --> " + postingUsid);
        }
        return posting.getContents(); 
    }

    @Override
    public void updateContents(PostingContents contents) {
        // 
        Posting posting = postingRepository.get(contents.getPostingUsid());
        posting.setContents(contents);
        
        postingRepository.put(posting);
    }

    //--------------------------------------------------------------------------
    // comment
    
    @Override
    public void createComment(String postingUsid, PostingComment comment) {
        // 
        Posting posting = postingRepository.get(postingUsid);
        posting.getContents().addComment(comment);
        
        postingRepository.update(posting);
    }

    @Override
    public PostingComment retrieveComment(String postingUsid, int sequence) throws EmptyResultException {
        // 
        Posting posting = postingRepository.get(postingUsid);
        if (posting == null || posting.getContents().getCommentBag().getComment(sequence) == null) {
            throw new EmptyResultException("No such a postingConents --> " + postingUsid);
        }
        return posting.getContents().getCommentBag().getComment(sequence); 
    }

    @Override
    public void deleteComment(String postingUsid, int sequence) {
        // 
        Posting posting = postingRepository.get(postingUsid);
        posting.getContents().removeComment(sequence);
        
        postingRepository.update(posting);
    }

    @Override
    public int nextCommentSequence(String postingUsid) {
        // 
        return seqStore.nextCommentSequence(postingUsid);
    }

    @Override
    public boolean isExistComment(String postingUsid, int sequence) {
        // 
        Posting posting = postingRepository.get(postingUsid);
        PostingComment comment = posting.getContents().getCommentBag().getComment(sequence);
        return comment != null;
    }
    
}
