/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:eschoi@nextree.co.kr">Choi, Eunsun</a>
 * @since 2015. 1. 9.
 */
package namoo.board.dom2.ui.web.controller.posting;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import namoo.board.dom2.entity.board.Posting;
import namoo.board.dom2.entity.board.SocialBoard;
import namoo.board.dom2.service.PostingService;
import namoo.board.dom2.service.SocialBoardService;
import namoo.board.dom2.shared.CommentCdo;
import namoo.board.dom2.ui.web.common.PageTransfer;
import namoo.board.dom2.ui.web.session.SessionManager;
import namoo.borad.pr.jersey.presenter.JerseyPresenterFactory;

@WebServlet("/posting/detail")
public class PostingDetailController extends HttpServlet {
    //
    private static final long serialVersionUID = 1419678229044433805L;
    
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //
        // SocialBoardService soicalBoardService = BoardServiceFactory.getInstance().createSocialBoardService();
        SocialBoardService socialBoardService = JerseyPresenterFactory.getBoardPresenter();;
        // PostingService postingService = BoardServiceFactory.getInstance().createPostingService();
        PostingService postingService = JerseyPresenterFactory.getPostingPresenter();
        
        // 로그인 체크
        if(!SessionManager.getInstance(req).isLogin()) {
            
            String message = SessionManager.LOGIN_REQUIRED_MESSAGE;
            String linkUrl = "/login";
            
            PageTransfer.getInstance(req, resp).error(message, linkUrl);
            return;
        }
        
        String boardUsid = req.getParameter("boardUsid");
        String postingUsid = req.getParameter("postingUsid");
        
        Posting posting = postingService.findPostingWithContents(postingUsid);
        SocialBoard socialBoard = socialBoardService.findSocialBoard(boardUsid);
        List<SocialBoard> socialBoards = socialBoardService.findAllSocialBoards();
        postingService.increasePostingReadCount(postingUsid);
        
        req.setAttribute("posting", posting);
        req.setAttribute("boardUsid", boardUsid);
        req.setAttribute("boardList", socialBoards);
        req.setAttribute("socialBoard", socialBoard);

        // request dispatch
        RequestDispatcher dispatcher =
                req.getRequestDispatcher("/WEB-INF/views/posting/detail.jsp");
        dispatcher.forward(req, resp);        
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //
        // PostingService postingService = BoardServiceFactory.getInstance().createPostingService();
        PostingService postingService = JerseyPresenterFactory.getPostingPresenter();
        
        String boardUsid = req.getParameter("boardUsid");
        String postingUsid = req.getParameter("postingUsid");
        
        String comment = req.getParameter("comment");
        String email = req.getParameter("email");
        
        CommentCdo commentCdo = new CommentCdo(comment, email);
        postingService.attachComment(postingUsid, commentCdo);
        
        // redirect
        String redirectUrl = getServletContext().getContextPath() + "/posting/detail?boardUsid="+boardUsid+"&postingUsid="+postingUsid;
        resp.sendRedirect(redirectUrl);
    }
}
