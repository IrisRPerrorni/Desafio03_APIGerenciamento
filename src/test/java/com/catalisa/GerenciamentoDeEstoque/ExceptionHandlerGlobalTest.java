package com.catalisa.GerenciamentoDeEstoque;

import com.catalisa.GerenciamentoDeEstoque.handle.ExceptionHandlerGlobal;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionHandlerGlobalTest {



        private final ExceptionHandlerGlobal exceptionHandler = new ExceptionHandlerGlobal();

        @Test
        public void testHandleIllegalArgumentException() {
            String errorMessage = "Erro de argumento inválido";
            IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

            ResponseEntity<String> response = exceptionHandler.handleIllegalArgumentException(exception);

            assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
            assert response.getBody().equals(errorMessage);
        }
    }


