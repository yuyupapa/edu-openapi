/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:eschoi@nextree.co.kr">Choi, Eunsun</a>
 * @since 2015. 1. 9.
 */
package namoo.board.dom2.ui.web.controller.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import namoo.board.dom2.entity.board.SocialBoard;
import namoo.board.dom2.service.SocialBoardService;
import namoo.board.dom2.ui.web.common.PageTransfer;
import namoo.board.dom2.ui.web.session.SessionManager;
import namoo.board.dom2.util.namevalue.NameValueList;
import namoo.borad.pr.jersey.presenter.JerseyPresenterFactory;

@WebServlet("/board")
public class BoardUpdateController extends HttpServlet {
    //
    private static final long serialVersionUID = 495098317447302613L;
    
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //
        // SocialBoardService soicalBoardService = BoardServiceFactory.getInstance().createSocialBoardService();
        SocialBoardService socialBoardService = JerseyPresenterFactory.getBoardPresenter(); 

        // 로그인 체크
        if(!SessionManager.getInstance(req).isLogin()) {
            
            String message = SessionManager.LOGIN_REQUIRED_MESSAGE;
            String linkUrl = "/login";
            
            PageTransfer.getInstance(req, resp).error(message, linkUrl);
            return;
        }
        
        String boardUsid = req.getParameter("boardUsid");
        
        SocialBoard socialBoard = socialBoardService.findSocialBoard(boardUsid);
        List<SocialBoard> socialBoards = socialBoardService.findAllSocialBoards();
        
        req.setAttribute("boardList", socialBoards);
        req.setAttribute("socialBoard", socialBoard);
        
        // request dispatch
        RequestDispatcher dispatcher =
                req.getRequestDispatcher("/WEB-INF/views/board/update.jsp");
        dispatcher.forward(req, resp);
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //
        // SocialBoardService soicalBoardService = BoardServiceFactory.getInstance().createSocialBoardService();
        SocialBoardService socialBoardService = JerseyPresenterFactory.getBoardPresenter(); 
        
        String boardUsid = req.getParameter("boardUsid");
        String boardName = req.getParameter("boardName");
        
        NameValueList nameValues = NameValueList.getInstance();            
        nameValues.add("name", boardName);
    
        socialBoardService.modifySocialBoard(boardUsid, nameValues);
        
        String message = "게시판이 수정되었습니다.";
        String confirmUrl = "/boards";

        PageTransfer.getInstance(req, resp).information(message, confirmUrl);
    }
}
