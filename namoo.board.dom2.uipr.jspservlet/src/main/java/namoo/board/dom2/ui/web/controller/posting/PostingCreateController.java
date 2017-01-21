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

import namoo.board.dom2.entity.board.SocialBoard;
import namoo.board.dom2.service.PostingService;
import namoo.board.dom2.service.SocialBoardService;
import namoo.board.dom2.shared.PostingCdo;
import namoo.board.dom2.ui.web.common.PageTransfer;
import namoo.board.dom2.ui.web.session.SessionManager;
import namoo.borad.pr.jersey.presenter.JerseyPresenterFactory;

@WebServlet("/posting/create")
public class PostingCreateController extends HttpServlet {
    //
    private static final long serialVersionUID = -3930852193813271610L;
    
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

        List<SocialBoard> socialBoards = socialBoardService.findAllSocialBoards();
        SocialBoard socialBoard = socialBoardService.findSocialBoard(boardUsid);
        Boolean commentable = socialBoard.isCommentable();
        
        req.setAttribute("commentable", commentable);
        req.setAttribute("boardUsid", boardUsid);
        req.setAttribute("boardList", socialBoards);
        
        // request dispatch
        RequestDispatcher dispatcher =
                req.getRequestDispatcher("/WEB-INF/views/posting/create.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //
        // PostingService postingService = BoardServiceFactory.getInstance().createPostingService();
        PostingService postingService = JerseyPresenterFactory.getPostingPresenter();
        
        String boardUsid = req.getParameter("boardUsid");
        String title = req.getParameter("title");
        String contents = req.getParameter("contents");
        String writerEmail = req.getParameter("writerEmail");
        String commentable = req.getParameter("commentable");
        String anonymousComment = req.getParameter("anonymousComment");
        
        Boolean comment = true;
        if("on".equals(commentable)) {
            comment = false;
        }
        
        Boolean anonymous = false;
        if("on".equals(anonymousComment)) {
            anonymous = true;
        }

        String message = null; 
        if(title != null && title.length() > 1) {
        	// 
	        PostingCdo postingCdo = new PostingCdo(boardUsid, title, contents, writerEmail);
	        postingCdo.setAnonymousComment(anonymous);
	        postingCdo.setCommentable(comment);
	        
	        postingService.registerPosting(postingCdo);
	        message = "게시글이 등록되었습니다.";
        } else {
        	message = "게시글에 적절하지 않습니다."; 
        }
        
        String confirmUrl = "/postings?boardUsid="+boardUsid+"&page=1";
        PageTransfer.getInstance(req, resp).information(message, confirmUrl);
    }
}