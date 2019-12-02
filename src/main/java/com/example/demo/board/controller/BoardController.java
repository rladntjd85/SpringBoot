package com.example.demo.board.controller;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.board.domain.BoardVO;
import com.example.demo.board.domain.FileVO;
import com.example.demo.board.service.BoardService;
 
@Controller
public class BoardController {
 
    @Resource(name="com.example.demo.board.service.BoardService")
    BoardService mBoardService;
    
    @Value("${file.upload.directory}")
    String uploadFileDir;

    @RequestMapping("/list")
    public String boardList(HttpServletRequest req, ModelMap modelMap, @ModelAttribute("boardSearchVO") BoardVO boardSearchVO) throws Exception {
//    String jspPath =req.getRequestURI();
        
    ///////paging : S//////////////////////////////
     
    int pageSize = boardSearchVO.getPageSize();// 한페이지에 나오는 게시물 개수
    int pageIndex = boardSearchVO.getPageIndex(); //현재 선택한 페이지 number
    int pageGroupSize = boardSearchVO.getPageGroupSize(); // 페이지 번호가 몇개 나오느냐 개수
    int startRow = (pageIndex - 1) * pageSize + 1;// 한 페이지의 시작글 번호 
    if(startRow == 1) { startRow = 0;}
    int endRow =  pageSize;// 한 페이지의 마지막 글번호
    
    System.out.println("startRow============" + startRow);
    System.out.println("============");
    System.out.println("endRow============ "+endRow);
    System.out.println("============");
 
    boardSearchVO.setStartRow(startRow);
    boardSearchVO.setEndRow(endRow);
    int count = mBoardService.boardCount(boardSearchVO); //게시물 총 개수
 
    int pageGroupCount = count / (pageSize * pageGroupSize) + (count % (pageSize * pageGroupSize) == 0 ? 0 : 1);
    int nowPageGroup = (int) Math.ceil((double) pageIndex / pageGroupSize);
     
    List<BoardVO> boardList = mBoardService.boardList(boardSearchVO);
     
    modelMap.put("pageIndex", pageIndex);
    modelMap.put("pageSize", pageSize);
    modelMap.put("count", count);
    modelMap.put("pageGroupSize", pageGroupSize);
    modelMap.put("nowPageGroup", nowPageGroup);
    modelMap.put("pageGroupCount", pageGroupCount);
    modelMap.put("articleList", boardList);
    modelMap.put("boardSearchVO", boardSearchVO);
     
        return "boardList";
    }


    @RequestMapping("/detail/{bno}") 
    private String boardDetail(@PathVariable int bno, Model model) throws Exception{
            
        model.addAttribute("detail", mBoardService.boardDetailService(bno));
        model.addAttribute("files", mBoardService.fileDetailService(bno)); //추가
            
        return "detail";
    }

    @RequestMapping("/insert") //게시글 작성폼 호출  
    private String boardInsertForm(){
        
        return "insert";
    }
    
    @RequestMapping("/insertProc")
    private String boardInsertProc(HttpServletRequest request, @RequestPart MultipartFile files) throws Exception{
        
        BoardVO board = new BoardVO();
        FileVO  file  = new FileVO();
        
        board.setSubject(request.getParameter("subject"));
        board.setContent(request.getParameter("content"));
        board.setWriter(request.getParameter("writer"));
        
        
        if(files.isEmpty()){
            mBoardService.boardInsertService(board); //게시글 insert
        }else{
            String fileName = files.getOriginalFilename(); 
            String fileNameExtension = FilenameUtils.getExtension(fileName).toLowerCase(); 
            File destinationFile; 
            String destinationFileName; 
            
            do { 
                destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + fileNameExtension; 
                destinationFile = new File(uploadFileDir+ destinationFileName); 
            } while (destinationFile.exists()); 
            
            destinationFile.getParentFile().mkdirs(); 
            files.transferTo(destinationFile); 
            
            mBoardService.boardInsertService(board); //게시글 insert
            
            file.setBno(board.getBno());
            file.setFileName(destinationFileName);
            file.setFileOriName(fileName);
            file.setFileUrl(uploadFileDir);
            
            mBoardService.fileInsertService(file); //file insert
        }
        
        
        return "redirect:/list";
    }
    
    @RequestMapping("/fileDown/{bno}")
    private void fileDown(@PathVariable int bno, HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        request.setCharacterEncoding("UTF-8");
        FileVO fileVO = mBoardService.fileDetailService(bno);
        
        //파일 업로드된 경로 
        try{
            String fileUrl = fileVO.getFileUrl();
            fileUrl += "/";
            String savePath = fileUrl;
            String fileName = fileVO.getFileName();
            
            //실제 내보낼 파일명 
            String oriFileName = fileVO.getFileOriName();
            InputStream in = null;
            OutputStream os = null;
            File file = null;
            boolean skip = false;
            String client = "";
            
            //파일을 읽어 스트림에 담기  
            try{
                file = new File(savePath, fileName);
                in = new FileInputStream(file);
            } catch (FileNotFoundException fe) {
                skip = true;
            }
            
            client = request.getHeader("User-Agent");
            
            //파일 다운로드 헤더 지정 
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Description", "JSP Generated Data");
            
            if (!skip) {
                // IE
                if (client.indexOf("MSIE") != -1) {
                    response.setHeader("Content-Disposition", "attachment; filename=\""
                            + java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
                    // IE 11 이상.
                } else if (client.indexOf("Trident") != -1) {
                    response.setHeader("Content-Disposition", "attachment; filename=\""
                            + java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
                } else {
                    // 한글 파일명 처리
                    response.setHeader("Content-Disposition",
                            "attachment; filename=\"" + new String(oriFileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
                    response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
                }
                response.setHeader("Content-Length", "" + file.length());
                os = response.getOutputStream();
                byte b[] = new byte[(int) file.length()];
                int leng = 0;
                while ((leng = in.read(b)) > 0) {
                    os.write(b, 0, leng);
                }
            } else {
                response.setContentType("text/html;charset=UTF-8");
                System.out.println("<script language='javascript'>alert('파일을 찾을 수 없습니다');history.back();</script>");
            }
            in.close();
            os.close();
        } catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
        }
        
    }
    
    @RequestMapping("/update/{bno}") //게시글 수정폼 호출  
    private String boardUpdateForm(@PathVariable int bno, Model model) throws Exception{
        
        model.addAttribute("detail", mBoardService.boardDetailService(bno));
        
        return "update";
    }
    
    @RequestMapping("/updateProc")
    private String boardUpdateProc(HttpServletRequest request) throws Exception{
        
        BoardVO board = new BoardVO();
        board.setSubject(request.getParameter("subject"));
        board.setContent(request.getParameter("content"));
        board.setBno(Integer.parseInt(request.getParameter("bno")));
        
        mBoardService.boardUpdateService(board);
        
        return "redirect:/detail/"+request.getParameter("bno"); 
    }
 
    @RequestMapping("/delete/{bno}")
    private String boardDelete(@PathVariable int bno) throws Exception{
        
        mBoardService.boardDeleteService(bno);
        
        return "redirect:/list";
    }
}