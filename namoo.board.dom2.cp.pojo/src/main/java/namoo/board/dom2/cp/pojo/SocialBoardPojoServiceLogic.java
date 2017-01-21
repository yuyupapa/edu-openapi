/*
 * COPYRIGHT (c) Nextree Consulting 2014
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:hkkang@nextree.co.kr">Kang Hyoungkoo</a>
 * @since 2014. 12. 11.
 */
package namoo.board.dom2.cp.pojo;

import java.util.List;

import namoo.board.dom2.entity.board.SocialBoard;
import namoo.board.dom2.lifecycle.BoardStoreLifecycler;
import namoo.board.dom2.logic.SocialBoardServiceLogic;
import namoo.board.dom2.service.SocialBoardService;
import namoo.board.dom2.util.namevalue.NameValueList;

public class SocialBoardPojoServiceLogic implements SocialBoardService {
    //
    private SocialBoardService service;

    
    public SocialBoardPojoServiceLogic(BoardStoreLifecycler storeLifecycler) {
        //
        this.service = new SocialBoardServiceLogic(storeLifecycler);
    }

    // -------------------------------------------------------------------------

    @Override
    public String registerBoard(String teamUsid, String boardName, boolean commentable) {
        // 
        return service.registerBoard(teamUsid, boardName, commentable);
    }


    @Override
    public SocialBoard findSocialBoard(String boardUsid) {
        // 
        return service.findSocialBoard(boardUsid);
    }


    @Override
    public List<SocialBoard> findAllSocialBoards() {
        // 
        return service.findAllSocialBoards();
    }


    @Override
    public void modifySocialBoard(String boardUsid, NameValueList nameValues) {
        // 
        service.modifySocialBoard(boardUsid, nameValues);
    }


    @Override
    public void removeSocialBoard(String boardUsid) {
        // 
        service.removeSocialBoard(boardUsid);
    }


}
