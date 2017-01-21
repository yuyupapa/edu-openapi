/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:eschoi@nextree.co.kr">Choi, Eunsun</a>
 * @since 2015. 1. 9.
 */
package namoo.board.dom2.ui.web.controller.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import namoo.board.dom2.service.SocialBoardService;
import namoo.board.dom2.ui.web.common.PageTransfer;
import namoo.borad.pr.jersey.presenter.JerseyPresenterFactory;

@WebServlet("/board/delete")
public class BoardDeleteController extends HttpServlet {
    //
    private static final long serialVersionUID = -3216928294490480848L;
    
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //
        // SocialBoardService soicalBoardService = BoardServiceFactory.getInstance().createSocialBoardService();
        SocialBoardService socialBoardService = JerseyPresenterFactory.getBoardPresenter(); 
        
        String boardUsid = req.getParameter("boardUsid");

        socialBoardService.removeSocialBoard(boardUsid);
        
        String message = "게시판이 삭제되었습니다.";
        String confirmUrl = "/boards";

        PageTransfer.getInstance(req, resp).information(message, confirmUrl);
    }
}
