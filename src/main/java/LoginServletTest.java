import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class LoginServletTest {

	// @Mock private OrderStartDAO dao;
	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private RequestDispatcher dispatcher;

}
