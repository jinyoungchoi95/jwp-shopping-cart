package woowacourse.shoppingcart.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static woowacourse.auth.acceptance.AuthAcceptanceTest.로그인_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.dto.customer.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.CustomerSignUpRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUpdatePasswordRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUpdateRequest;

@DisplayName("회원 관련 기능")
public class CustomerAcceptanceTest extends AcceptanceTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password123";
    private static final String PHONE_NUMBER = "01012345678";
    private static final String ADDRESS = "성담빌딩";

    @DisplayName("회원가입")
    @Nested
    class AddCustomer extends AcceptanceTest {

        private CustomerSignUpRequest request;

        @BeforeEach
        void prepare() {
            request = new CustomerSignUpRequest(USERNAME, PASSWORD, PHONE_NUMBER, ADDRESS);
        }

        @DisplayName("성공한다.")
        @Test
        void success() {
            ExtractableResponse<Response> response = 회원_가입_요청(request);
            회원_가입됨(response);
        }

        @DisplayName("중복된 유저 이름으로 가입하여 실패")
        @Test
        void uplicatedUsername_fail() {
            회원_가입_요청(request);

            ExtractableResponse<Response> response = 회원_가입_요청(request);
            회원_가입_실패(response);
        }
    }

    @DisplayName("내 정보 조회")
    @Test
    void getMe() {
        String accessToken = 회원_가입_후_로그인();

        ExtractableResponse<Response> response = 내_정보_조회(accessToken);
        정보_조회_성공(response, new CustomerResponse(USERNAME, PHONE_NUMBER, ADDRESS));
    }

    @DisplayName("내 정보 수정")
    @Test
    void updateMe() {
        String accessToken = 회원_가입_후_로그인();
        CustomerUpdateRequest request = new CustomerUpdateRequest("01087654321", "루터회관");

        ExtractableResponse<Response> response = 회원_정보_수정_요청(accessToken, request);
        회원_정보_수정_성공(response);
    }

    @DisplayName("내 비밀번호 수정")
    @Test
    void updatePassword() {
        String accessToken = 회원_가입_후_로그인();
        CustomerUpdatePasswordRequest request = new CustomerUpdatePasswordRequest("changedPassword123");

        ExtractableResponse<Response> response = 회원_비밀번호_수정_요청(accessToken, request);
        회원_비밀번호_수정_성공(response);
    }

    @DisplayName("회원탈퇴")
    @Test
    void deleteMe() {
        String accessToken = 회원_가입_후_로그인();

        ExtractableResponse<Response> response = 회원_탈퇴_요청(accessToken);
        회원_탈퇴_성공(response);
    }

    public static ExtractableResponse<Response> 회원_가입_요청(final CustomerSignUpRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/customers/signup")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_조회(final String accessToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + accessToken)
                .when().get("/api/customers")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(final String accessToken,
                                                            final CustomerUpdateRequest request) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/api/customers")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_비밀번호_수정_요청(final String accessToken,
                                                            final CustomerUpdatePasswordRequest request) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().patch("/api/customers/password")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_탈퇴_요청(final String accessToken) {
        return RestAssured.given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .when().delete("/api/customers")
                .then().log().all()
                .extract();
    }

    private String 회원_가입_후_로그인() {
        CustomerSignUpRequest request = new CustomerSignUpRequest(USERNAME, PASSWORD, PHONE_NUMBER, ADDRESS);
        회원_가입_요청(request);
        TokenRequest tokenRequest = new TokenRequest(USERNAME, PASSWORD);
        return 로그인_요청(tokenRequest).body()
                .as(TokenResponse.class)
                .getAccessToken();
    }

    private void 회원_가입됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 회원_가입_실패(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 정보_조회_성공(final ExtractableResponse<Response> response, final CustomerResponse expected) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().as(CustomerResponse.class))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private void 회원_정보_수정_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 회원_비밀번호_수정_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 회원_탈퇴_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
