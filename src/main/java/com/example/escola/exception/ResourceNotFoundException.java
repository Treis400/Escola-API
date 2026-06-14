package com.example.escola.exception;

// Exceção customizada que representa o caso "recurso não encontrado" (HTTP 404).
// Estende RuntimeException para não precisar ser declarada nos métodos com "throws"
// — o Spring captura exceções não verificadas automaticamente.
// O GlobalExceptionHandler escuta exatamente este tipo e monta a resposta 404.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
