/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dal.CourseDAO;
import dal.Impl.CourseDAOImpl;
import dal.Impl.LessonDAOImpl;
import dal.LessonDAO;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.stream.Collectors;
import model.Course;
import model.chapters;
import model.lessons;
import static utils.Constans.LinkYouTube;
import static utils.Constans.LinkYouTubeEmbed;
import static utils.Constans.PAGINATION_DEFAULT_PAGE_SIZE;



/**
 *
 * @author MH
 */
@MultipartConfig
@WebServlet(name = "addLesson", urlPatterns = {"/addLesson"})
public class addLesson extends HttpServlet {

    private static final String VIEW_PATH_END = "/Dashboard/admin/LessonList.jsp";
    
    private static final String VIEW_PATH_START = "/Dashboard/admin/LessonDetail.jsp";

    private final LessonDAO daoLesson = new LessonDAOImpl();

    private final CourseDAO daoCourse = new CourseDAOImpl();

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        int courseID = Integer.parseInt(request.getParameter("courseId"));
        Course course = daoCourse.getCourseByCourseId(courseID);
        request.setAttribute("course", course);
        
        int chapterID = Integer.parseInt(request.getParameter("chapterId"));
        chapters chapter = daoLesson.getChapterByChapterId(chapterID);
        request.setAttribute("chapter", chapter);
        
        String action = request.getParameter("action");
        request.setAttribute("action", action);
        

        
        request.setAttribute("actionLesson", "add");
        
        request.getRequestDispatcher(VIEW_PATH_START).forward(request, response);
    }
    

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String message = "";
            
            String action = request.getParameter("action");
            request.setAttribute("action", action);
            
            int courseID = Integer.parseInt(request.getParameter("courseId"));
            Course course = daoCourse.getCourseByCourseId(courseID);
            request.setAttribute("course", course);
            
           
            
            
            String pageIndexS = request.getParameter("page")==null?"1":request.getParameter("page");
            int pageIndex = Integer.parseInt(pageIndexS);
            request.setAttribute("pageIndex", pageIndex);
            
            
            int chapterID = Integer.parseInt(request.getParameter("chapterId"));
            chapters chapter = daoLesson.getChapterByChapterId(chapterID);
            request.setAttribute("chapter", chapter);
            
       //add Lesson
        String name = request.getParameter("name");
        int status = Integer.parseInt(request.getParameter("status")==null ? "1" : request.getParameter("status"));
        String content_text = request.getParameter("description");
        
        String video = request.getParameter("video");
        String youtubeLink="";
        
//        int srcIndex = video.indexOf("src=\"");
//        if (srcIndex != -1) {
//            int endIndex = video.indexOf("\"", srcIndex + 5); 
//            if (endIndex != -1) {
//                
//                youtubeLink = video.substring(srcIndex + 5, endIndex);
//                
//            }
//        }

        if (video.contains(LinkYouTubeEmbed)) {
            youtubeLink= daoLesson.getLinkVideoEmbed(video);
        }
        if (video.contains(LinkYouTube)) {
            youtubeLink=daoLesson.getLinkVideo(video);
        }
        
        lessons lessonAdd = new lessons(0, name, chapterID, youtubeLink, status, content_text);
        daoLesson.insertLesson(lessonAdd);
        
        message="Add Lesson successful!"; 
            

        int numberOfLesson = daoLesson.countLessonByChapterID(chapterID,1);
        
            
        request.setAttribute("numberOfLesson", numberOfLesson);

        
        int endPage = 0;

        endPage = numberOfLesson / PAGINATION_DEFAULT_PAGE_SIZE;
        if (numberOfLesson % PAGINATION_DEFAULT_PAGE_SIZE != 0) {
            endPage = endPage + 1;
        }
        if (pageIndex > endPage) {
            pageIndex = endPage;
        }

        request.setAttribute("endPage", endPage);
        
        
        
        //end Lesson
        
        List<lessons> listLesson = daoLesson.getLessonsByChapterID(chapterID);            
        
        listLesson = listLesson.stream().filter(lessons->lessons.getStatus()==1)
                .skip(0) 
                .limit(PAGINATION_DEFAULT_PAGE_SIZE)
                .collect(Collectors.toList());
        
        
        request.setAttribute("listLesson", listLesson);

        request.setAttribute("message", message);        
        
        request.getRequestDispatcher(VIEW_PATH_END).forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
