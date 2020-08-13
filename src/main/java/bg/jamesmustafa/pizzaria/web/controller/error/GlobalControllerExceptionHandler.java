package bg.jamesmustafa.pizzaria.web.controller.error;

import bg.jamesmustafa.pizzaria.error.common.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final String DEFAULT_ERROR_VIEW = "error/error";
    private static final String ACCESS_DENIED_ERROR_VIEW = "error/errorForbidden";
    private static final String NOT_FOUND_ERROR_VIEW = "error/errorNotFound";
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        LOGGER.error("EXCEPTION: " + e.getMessage() + ", AT URL: " + req.getRequestURL() + ", Stack trace: " + e);
        ModelAndView mav = new ModelAndView();
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ModelAndView accessDeniedErrorHandler(HttpServletRequest req, AccessDeniedException e) throws AccessDeniedException{
        LOGGER.error("EXCEPTION: " + e.getMessage() + ", AT URL: " + req.getRequestURL());
        ModelAndView mav = new ModelAndView();
        mav.setViewName(ACCESS_DENIED_ERROR_VIEW);
        return mav;
    }


    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView notFoundErrorHandler(HttpServletRequest req, NotFoundException e) throws NotFoundException{
        LOGGER.error("EXCEPTION: " + e.getMessage() + ", AT URL: " + req.getRequestURL());
        ModelAndView mav = new ModelAndView();
        mav.setViewName(NOT_FOUND_ERROR_VIEW);
        return mav;
    }

}
