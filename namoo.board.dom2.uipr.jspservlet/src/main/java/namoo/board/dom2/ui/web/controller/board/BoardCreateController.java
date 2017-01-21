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
import namoo.board.dom2.entity.user.BoardTeam;
import namoo.board.dom2.service.BoardUserService;
import namoo.board.dom2.service.SocialBoardService;
import namoo.board.dom2.ui.web.common.PageTransfer;
import namoo.board.dom2.ui.web.session.SessionManager;
import namoo.borad.pr.jersey.presenter.JerseyPresenterFactory;

@WebServlet("/board/create")
public class BoardCreateController extends HttpServlet {
    //
    private static final long serialVersionUID = 495098317447302613L;
    
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //
        // SocialBoardService soicalBoardService = BoardServiceFactory.getInstance().createSocialBoardService();
        // BoardUserService boardUserService = BoardServiceFactory.getInstance().createBoardUserService();
        SocialBoardService socialBoardService = JerseyPresenterFactory.getBoardPresenter(); 
        BoardUserService boardUserService = JerseyPresenterFactory.getBoardUserPresenter(); 
        
        // 로그인 체크
        if(!SessionManager.getInstance(req).isLogin()) {
            
            String message = SessionManager.LOGIN_REQUIRED_MESSAGE;
            String linkUrl = "/login";
            
            PageTransfer.getInstance(req, resp).error(message, linkUrl);
            return;
        }
        
        List<BoardTeam> teams = boardUserService.findAllBoardTeams();
        List<SocialBoard> socialBoards = socialBoardService.findAllSocialBoards();
        
        req.setAttribute("boardList", socialBoards);
        req.setAttribute("teams", teams);
    
        // request dispatch
        RequestDispatcher dispatcher =
                req.getRequestDispatcher("/WEB-INF/views/board/create.jsp");
        dispatcher.forward(req, resp);
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //
        // SocialBoardService soicalBoardService = BoardServiceFactory.getInstance().createSocialBoardService();
        SocialBoardService socialBoardService = JerseyPresenterFactory.getBoardPresenter(); 
        
        String teamUsid = req.getParameter("teamUsid");
        String boardName = req.getParameter("boardName");
        String commentable = req.getParameter("commentable");
        
        Boolean comment = false;
        if("on".equals(commentable)) {
            comment = true;
        }
        
        socialBoardService.registerBoard(teamUsid, boardName, comment);
        
        String message = "게시판 생성이 완료되었습니다.";
        String confirmUrl = "/boards";

        PageTransfer.getInstance(req, resp).information(message, confirmUrl);
    }
}
