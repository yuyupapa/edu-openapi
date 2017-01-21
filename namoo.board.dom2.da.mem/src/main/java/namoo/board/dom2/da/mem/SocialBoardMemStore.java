/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:hkkang@nextree.co.kr">Kang Hyoungkoo</a>
 * @since 2015. 1. 15.
 */
package namoo.board.dom2.da.mem;

import java.util.List;

import namoo.board.dom2.da.mem.mapstore.SocialBoardRepository;
import namoo.board.dom2.da.mem.mapstore.UsidSeqRepository;
import namoo.board.dom2.entity.board.SocialBoard;
import namoo.board.dom2.store.SocialBoardStore;
import namoo.board.dom2.util.exception.EmptyResultException;

public class SocialBoardMemStore implements SocialBoardStore {
    //
    private SocialBoardRepository boardStore = SocialBoardRepository.getInstance();
    private UsidSeqRepository seqStore = UsidSeqRepository.getInstance();
    
    //--------------------------------------------------------------------------
    
    @Override
    public String create(SocialBoard board) {
        // 
        boardStore.put(board);
        return board.getUsid();
    }

    @Override
    public SocialBoard retrieve(String usid) throws EmptyResultException {
        // 
        SocialBoard board = boardStore.get(usid); 
        if (board == null) {
            throw new EmptyResultException("No such a board --> " + usid);
        }
        return board;
    }

    @Override
    public SocialBoard retrieveByName(String name) throws EmptyResultException {
        //
        SocialBoard board = boardStore.getByName(name); 
        if (board == null) {
            throw new EmptyResultException("No such a board --> " + name);
        }
        return board;
    }

    @Override
    public List<SocialBoard> retrieveAll() {
        // 
        return boardStore.getAll();
    }

    @Override
    public void update(SocialBoard board) {
        // 
        boardStore.update(board);
    }

    @Override
    public void delete(String usid) {
        // 
        boardStore.remove(usid);
    }

    @Override
    public int nextSequence() {
        // 
        return seqStore.nextBoardSequence();
    }

    @Override
    public boolean isExist(String usid) {
        // 
        return boardStore.get(usid) != null;
    }

    @Override
    public boolean isExistByName(String name) {
        // 
        return boardStore.getByName(name) != null;
    }
    
}
