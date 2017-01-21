/*
 * COPYRIGHT (c) Nextree Consulting 2015
 * This software is the proprietary of Nextree Consulting.  
 * 
 * @author <a href="mailto:hkkang@nextree.co.kr">Kang Hyoungkoo</a>
 * @since 2015. 1. 9.
 */
package namoo.board.dom2.ui.cli;

import java.io.File;
import java.net.URL;
import java.util.List;

import namoo.board.dom2.entity.user.BoardMember;
import namoo.board.dom2.entity.user.BoardTeam;
import namoo.board.dom2.entity.user.BoardUser;
import namoo.board.dom2.service.BoardUserService;

public class BoardUserCLI {
    //
    private ExcelFileLoader excelLoader;
    private BoardUserService userService;
    
    //--------------------------------------------------------------------------

    public BoardUserCLI(BoardUserService userService) {
        //
        this.excelLoader = new ExcelFileLoader() ; 
        this.userService = userService;
    }
    
    //--------------------------------------------------------------------------
    
    public void executeExcelLoader() {
        //
        URL path = this.getClass().getResource("/BoardUsers.xlsx");
        boolean result = excelLoader.registerServiceUsers(new File(path.getPath()));
        
        System.out.println("Excel Loader was executed. --> " + result);
    }
    
    public void findAllBoardUsers() {
        //
        System.out.println("====================================================");
        System.out.println(" 1. BoardUser 전체목록 조회");
        System.out.println("====================================================");
        
        List<BoardUser> allUsers = userService.findAllUsers();
        
        if (allUsers.isEmpty()) {
            InputScanner.getInstance().waitInputEnter("Not any user.");
            return;
        }
        
        System.out.println("====================================================");
        
        for (BoardUser user : allUsers) {
            //
            System.out.println(user.toString());
        }
        InputScanner.getInstance().waitInputEnter("User inquiry is complete.");
    }
    
    public void findAllBoardTeams() {
        //
        System.out.println("====================================================");
        System.out.println(" 2. BoardTeam 전체목록 조회");
        System.out.println("====================================================");
        
        List<BoardTeam> allTeams = userService.findAllBoardTeams();
        
        if (allTeams.isEmpty()) {
            InputScanner.getInstance().waitInputEnter("Not any team.");
            return;
        }
        
        System.out.println("====================================================");
        
        for (BoardTeam team : allTeams) {
            //
            System.out.println(team.toString());
        }
        InputScanner.getInstance().waitInputEnter("Team inquiry completed.");
    }
    
    public void findTeamMembers() {
        //
        System.out.println("====================================================");
        System.out.println(" 3. TeamMember 목록 조회");
        System.out.println("====================================================");
        
        String teamUsid = InputScanner.getInstance().getStringInput("Team id");

        List<BoardMember> allMembers = userService.findTeamBoardMembers(teamUsid);
        
        if (allMembers.isEmpty()) {
            InputScanner.getInstance().waitInputEnter("No such a team --> " + teamUsid + " or not any member.");
            return;
        }
        
        System.out.println("====================================================");
        
        for (BoardMember member : allMembers) {
            //
            System.out.println(member.toString());
        }
        InputScanner.getInstance().waitInputEnter("Member inquiry is complete.");
    }
}
