package co.com.bancolombia.api.commons;

import co.com.bancolombia.api.commons.enums.RequiredQueryParam;
import co.com.bancolombia.api.properties.PageDefaultProperties;
import co.com.bancolombia.model.commons.PageRequest;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.report.AvailableFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@WebFluxTest(properties = {
        "settings.pagination.defaultPageSize=10",
        "settings.pagination.defaultPageNumber=1"
})
@ContextConfiguration(classes = {
        PageDefaultProperties.class
})
class RequestHelperTest {

    public static final String CSV = "CSV";
    public static final String TXT = "TXT";
    public static final String INVALID_FORMAT = "invalidFormat";
    public static final String FORMAT = "format";
    @Autowired
    private PageDefaultProperties pageDefaultProperties;

    @Mock
    private ServerRequest serverRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @EnumSource(RequiredQueryParam.class)
    void shouldGetQueryParamSuccessFully(RequiredQueryParam requiredQueryParam) {
        String paramValue = "paramValue";
        when(serverRequest.queryParam(requiredQueryParam.getValue())).thenReturn(Optional.of(paramValue));
        assertEquals(paramValue, RequestHelper.getQueryParam(serverRequest, requiredQueryParam));
        verify(serverRequest).queryParam(requiredQueryParam.getValue());
        verifyNoMoreInteractions(serverRequest);
    }

    @ParameterizedTest
    @EnumSource(RequiredQueryParam.class)
    void shouldThrowBadRequestExceptionWhenQueryParamDoesNotExist(RequiredQueryParam requiredQueryParam) {
        when(serverRequest.queryParam(requiredQueryParam.getValue())).thenReturn(java.util.Optional.empty());
        assertThrows(BusinessException.class, () -> RequestHelper.getQueryParam(serverRequest, requiredQueryParam),
                BusinessErrorMessage.valueOf(requiredQueryParam.getBusinessErrorMessage().name()).getMessage());
        verify(serverRequest).queryParam(requiredQueryParam.getValue());
        verifyNoMoreInteractions(serverRequest);
    }

    @Test
    void shouldGetPageRequestParamSuccessFully(){
        Integer paramPageNumberValue = 5;
        Integer paramPageSizeValue = 20;

        var expectedPageRequest = new PageRequest(paramPageNumberValue, paramPageSizeValue);

        when(serverRequest.queryParam("pageNumber")).thenReturn(Optional.of(paramPageNumberValue.toString()));
        when(serverRequest.queryParam("pageSize")).thenReturn(Optional.of(paramPageSizeValue.toString()));

        var pageRequest = RequestHelper.getPageRequest(serverRequest, pageDefaultProperties);

        assertEquals(expectedPageRequest.getNumber(), pageRequest.getNumber());
        assertEquals(expectedPageRequest.getSize(), pageRequest.getSize());
    }

    @Test
    void shouldGetDefaultPageRequestParamSuccessFully(){
        Integer paramPageNumberValue = 1;
        Integer paramPageSizeValue = 10;

        var expectedPageRequest = new PageRequest(paramPageNumberValue, paramPageSizeValue);

        when(serverRequest.queryParam("pageNumber")).thenReturn(Optional.empty());
        when(serverRequest.queryParam("pageSize")).thenReturn(Optional.empty());

        var pageRequest = RequestHelper.getPageRequest(serverRequest, pageDefaultProperties);

        assertEquals(expectedPageRequest.getNumber(), pageRequest.getNumber());
        assertEquals(expectedPageRequest.getSize(), pageRequest.getSize());
    }

    @Test
    void shouldGetAvailableFormatCSVHeaderSuccessFully(){
        ServerRequest.Headers headers = mock(ServerRequest.Headers.class);
        when(headers.header(FORMAT)).thenReturn(Arrays.asList(CSV));
        when(serverRequest.headers()).thenReturn(headers);

        AvailableFormat result = RequestHelper.getAvailableFormatHeader(serverRequest);
        assertEquals(AvailableFormat.CSV, result);
    }

    @Test
    void shouldGetAvailableFormatTXTHeaderSuccessFully(){
        ServerRequest.Headers headers = mock(ServerRequest.Headers.class);
        when(headers.header(FORMAT)).thenReturn(Arrays.asList(TXT));

        when(serverRequest.headers()).thenReturn(headers);

        AvailableFormat result = RequestHelper.getAvailableFormatHeader(serverRequest);
        assertEquals(AvailableFormat.TXT, result);
    }

    @Test
    void shouldGetAvailableFormatHeaderError(){
        ServerRequest.Headers headers = mock(ServerRequest.Headers.class);
        when(headers.header(FORMAT)).thenReturn(Arrays.asList(INVALID_FORMAT));

        when(serverRequest.headers()).thenReturn(headers);

        assertThrows(BusinessException.class, () -> RequestHelper.getAvailableFormatHeader(serverRequest),
                BusinessErrorMessage.REQUIRED_FORMAT.getMessage());
    }
}
