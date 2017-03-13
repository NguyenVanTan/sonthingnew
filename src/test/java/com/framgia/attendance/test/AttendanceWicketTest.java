package com.framgia.attendance.test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AttendanceWicketTest extends AttendanceTest {

    public static final String USERNAME_LOCAL = "framgia";
    public static final String USERNAME_GLOBAL =
            "hideyuki.takeuchi@framgia.com";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected AttendanceWicketTester tester = null;

    @Before
    public void setUp() {
        tester = new AttendanceWicketTester();
        signInByGlobalUser();
    }

    @After
    public void tearDown() {
        if (tester != null) {
            tester.destroy();
            tester = null;
        }
    }

    /**
     * グローバル契約無しのユーザでサインインしたテスターの取得
     */
    protected void signInByLocalUser() {
        signInForTest(USERNAME_LOCAL);
    }

    /**
     * グローバル契約有りのユーザでサインインしたテスターの取得
     */
    protected void signInByGlobalUser() {
        signInForTest(USERNAME_GLOBAL);
    }

    /**
     * テストの為に高速化されたサインイン手続き
     */
    protected void signInForTest(String username) {
        assertThat("WicketTesterが初期化されていない", tester, is(notNullValue()));
//
//        UserAdminDao userAdminDao =
//                SingletonS2Container.getComponent(UserAdminDao.class);
//        UserAdmin userAdmin = userAdminDao.selectByLoginname(username);
//
//        assertThat("セッションに使用するアカウントが見つからない: " + username, userAdmin,
//                is(notNullValue()));
//
//        UserDto userDto = SingletonS2Container.getComponent(UserDto.class);
//        userDto.authenticate(userAdmin);
    }

    /**
     * 正しいサインイン手続き。ログインログも記録される。
     */
    protected void signInWithProperProcedure(String username, String password) {
        assertThat("WicketTesterが初期化されていない", tester, is(notNullValue()));

//        SpeedaSession session = (SpeedaSession) tester.getWicketSession();
//        Map<String, String> parameters = new HashMap<String, String>();
//        parameters.put(SpeedaSession.AUTH_USERNAME, username);
//        parameters.put(SpeedaSession.AUTH_PASSWORD, password);
//        parameters.put(SpeedaSession.AUTH_FORCELOGIN, "true");
//        parameters.put(SpeedaSession.AUTH_TYPE, LoginType.SPEEDA.toString());
//        session.signIn(parameters);
    }
}
