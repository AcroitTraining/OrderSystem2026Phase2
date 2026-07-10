package servlet;

import java.io.IOException;
import java.net.URLEncoder;

import dao.LoginDAO;  // インポートをLoginDAOに変更
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.LoginInfo;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	private LoginDAO dao = new LoginDAO();

	public void setLoginDAO(LoginDAO dao) {
		this.dao = dao;
	}

	private LoginInfo loginInfo;
	public LoginInfo getLoginInfo() { return loginInfo; }

	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


		// 1. 画面からの入力値を取得
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");

		// 2. 未入力チェック (バリデーション)
		if (userId == null || userId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			String safeId = (userId != null) ? URLEncoder.encode(userId, "UTF-8") : "";
			response.sendRedirect("index.jsp?error=empty&userId=" + safeId);
			return;
		}
		// 遷移先へ渡すデータ
		loginInfo = new LoginInfo(userId, password);
			LoginInfo testInfo = dao.loginCheck(userId, password);
			if(testInfo != null){
				HttpSession session = request.getSession();
				session.setAttribute("loginUser", testInfo);
				response.sendRedirect("HomeServlet");
			} else {
				// ログイン失敗：エラーパラメータをつけてログイン画面に戻す
				response.sendRedirect("index.jsp?error=wrong");
			}
	}
}