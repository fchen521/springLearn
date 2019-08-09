import com.example.Application;
import com.example.dbutils.QueryRunnerCRUD;
import com.example.mapper.UserInfoMapper;
import com.example.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserInfoTest{

    @Autowired
    UserInfoMapper userInfoMapper;

    @Test
    public void userTest() throws SQLException {
        List<UserInfo> infos = QueryRunnerCRUD.query("SELECT * FROM userinfo", UserInfo.class);
        infos.forEach(x->{
            System.out.println(infos.get(1));
        });
    }
}
