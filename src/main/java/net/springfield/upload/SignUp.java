package net.springfield.upload;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase; 

import org.bson.Document; 
import com.mongodb.MongoClient; 

/**
 * Servlet implementation class Login
 */
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		
		String user = request.getParameter("user");    
	    String pwd = request.getParameter("pass");
	    String name = request.getParameter("name");
	    String email = request.getParameter("email");
	    
	    
	    MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	    MongoDatabase database = mongo.getDatabase("springfield"); 
	    MongoCollection<Document> collection = database.getCollection("registration");
	    Document document = new Document("Name", name)
	    .append("Email", email)
	    .append("UserName", user)
	    .append("Password", pwd);
	    collection.insertOne(document);
	    
	    MongoCollection<Document> ucollection = database.getCollection(user);
	    
	    getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
	    
	    
	    
	}

}
