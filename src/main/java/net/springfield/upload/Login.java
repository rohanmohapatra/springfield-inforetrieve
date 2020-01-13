package net.springfield.upload;

import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase; 

import org.bson.Document; 
import com.mongodb.MongoClient; 
import static com.mongodb.client.model.Filters.eq;
/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	*/
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String user = request.getParameter("loginuser");    
	    String pwd = request.getParameter("loginpass");
	    boolean user_found = false;

	    
	    MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	    MongoDatabase database = mongo.getDatabase("springfield"); 
	    MongoCollection<Document> collection = database.getCollection("registration");
	    
	    List obj = new ArrayList();
	    obj.add(new BasicDBObject("UserName", user));
	    obj.add(new BasicDBObject("Password", pwd));
	    BasicDBObject whereQuery = new BasicDBObject();
	    whereQuery.put("$and", obj);
	    FindIterable<Document> cursor = collection.find(whereQuery);
	    for(Document doc : cursor) {
	    	            System.out.println("Found?= " + doc);
	    	            user_found = true;
	    	            //getServletContext().getRequestDispatcher("/upload.jsp").forward(request, response);
	    	        }
	    if(user_found)
	    {
	    	HttpSession session = request.getSession(true);
	        session.setAttribute("UserName", user);
	    	getServletContext().getRequestDispatcher("/upload.jsp").forward(request, response);
	    }
	    else
	    {
	    	getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
	    }

}
}
