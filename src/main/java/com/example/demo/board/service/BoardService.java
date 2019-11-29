package com.example.demo.board.service;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
 
import org.springframework.stereotype.Service;
 
import com.example.demo.board.domain.BoardVO;
import com.example.demo.board.domain.FileVO;
import com.example.demo.board.mapper.BoardMapper;
 
@Service("com.example.demo.board.service.BoardService")
public class BoardService {
 
    @Resource(name="com.example.demo.board.mapper.BoardMapper")
    BoardMapper mBoardMapper;
    
//    public List<BoardVO> boardListService() throws Exception{
//        
//        return mBoardMapper.boardList();
//    }
//    
 // paging
    public Map<String, Object> boardList(int currentPage) throws Exception{
        
        // 페이지에 보여줄 행의 개수 ROW_PER_PAGE = 10으로 고정
        final int ROW_PER_PAGE = 10; 
        
        // 페이지에 보여줄 첫번째 페이지 번호는 1로 초기화
        int startPageNum = 1;
        
        // 처음 보여줄 마지막 페이지 번호는 10
        int lastPageNum = ROW_PER_PAGE;
        
        // 현재 페이지가 ROW_PER_PAGE/2 보다 클 경우
        if(currentPage > (ROW_PER_PAGE/2)) {
            // 보여지는 페이지 첫번째 페이지 번호는 현재페이지 - ((마지막 페이지 번호/2) -1 )
            // ex 현재 페이지가 6이라면 첫번째 페이지번호는 2
            startPageNum = currentPage - ((lastPageNum/2)-1);
            // 보여지는 마지막 페이지 번호는 현재 페이지 번호 + 현재 페이지 번호 - 1 
            lastPageNum += (startPageNum-1);
        }
        
        // Map Data Type 객체 참조 변수 map 선언
        // HashMap() 생성자 메서드로 새로운 객체를 생성, 생성된 객체의 주소값을 객체 참조 변수에 할당
        Map<String, Integer> map = new HashMap<String, Integer>();
        // 한 페이지에 보여지는 첫번째 행은 (현재페이지 - 1) * 10
        int startRow = (currentPage - 1)*ROW_PER_PAGE;
        // 값을 map에 던져줌
        map.put("startRow", startRow);
        map.put("rowPerPage", ROW_PER_PAGE);
        
        // DB 행의 총 개수를 구하는 getBoardAllCount() 메서드를 호출하여 double Date Type의 boardCount 변수에 대입
        double boardCount = mBoardMapper.boardCount();
        
        // 마지막 페이지번호를 구하기 위해 총 개수 / 페이지당 보여지는 행의 개수 -> 올림 처리 -> lastPage 변수에 대입
        int lastPage = (int)(Math.ceil(boardCount/ROW_PER_PAGE));
        
        // 현재 페이지가 (마지막 페이지-4) 보다 같거나 클 경우
        if(currentPage >= (lastPage-4)) {
            // 마지막 페이지 번호는 lastPage
            lastPageNum = lastPage;
        }
        
        // 구성한 값들을 Map Date Type의 resultMap 객체 참조 변수에 던져주고 return
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("list", mBoardMapper.boardList(map));
        resultMap.put("currentPage", currentPage);
        resultMap.put("lastPage", lastPage);
        resultMap.put("startPageNum", startPageNum);
        resultMap.put("lastPageNum", lastPageNum);
        
        System.out.println("currentPage"+currentPage);
        System.out.println("lastPage"+lastPage);
        System.out.println("startPageNum"+startPageNum);
        System.out.println("lastPageNum"+lastPageNum);
        System.out.println("=========");
        System.out.println("=========");
        return resultMap;
    }
    
    public BoardVO boardDetailService(int bno) throws Exception{
        
        return mBoardMapper.boardDetail(bno);
    }
    
    public int boardInsertService(BoardVO board) throws Exception{
        
        return mBoardMapper.boardInsert(board);
    }
    
    public int boardUpdateService(BoardVO board) throws Exception{
        
        return mBoardMapper.boardUpdate(board);
    }
    
    public int boardDeleteService(int bno) throws Exception{
        
        return mBoardMapper.boardDelete(bno);
    }
    
    public int fileInsertService(FileVO file) throws Exception{
        return mBoardMapper.fileInsert(file);
    }
    
    public FileVO fileDetailService(int bno) throws Exception{
        
        return mBoardMapper.fileDetail(bno);
    }
}