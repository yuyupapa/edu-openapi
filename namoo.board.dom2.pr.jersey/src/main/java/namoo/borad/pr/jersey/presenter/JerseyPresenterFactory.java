package namoo.borad.pr.jersey.presenter;

import namoo.board.dom2.service.SocialBoardService;
import namoo.borad.pr.jersey.presenter.board.SocialBoardPresenter;
import namoo.borad.pr.jersey.presenter.post.PostingPresenter;
import namoo.borad.pr.jersey.presenter.user.BoardUserPresenter;

public class JerseyPresenterFactory {
	//
	public static SocialBoardService getBoardPresenter() {
		// 
		return new SocialBoardPresenter(); 
	}
	
	public static PostingPresenter getPostingPresenter() {
		// 
		return new PostingPresenter(); 
	}
	
	public static BoardUserPresenter getBoardUserPresenter() {
		// 
		return new BoardUserPresenter(); 
	}
}