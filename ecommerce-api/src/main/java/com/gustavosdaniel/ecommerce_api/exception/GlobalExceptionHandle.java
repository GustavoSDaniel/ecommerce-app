package com.gustavosdaniel.ecommerce_api.exception;

import com.gustavosdaniel.ecommerce_api.address.AddressNotFoundException;
import com.gustavosdaniel.ecommerce_api.category.CategoryNotFoundException;
import com.gustavosdaniel.ecommerce_api.category.NameCategoryExistException;
import com.gustavosdaniel.ecommerce_api.notification.NotificationNotFoundException;
import com.gustavosdaniel.ecommerce_api.order.*;
import com.gustavosdaniel.ecommerce_api.payment.*;
import com.gustavosdaniel.ecommerce_api.product.*;
import com.gustavosdaniel.ecommerce_api.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandle {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        log.warn("Validation failed {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        ErrorResponse erros = new ErrorResponse("Validação falhou",
                "Erro de validação nos campos",
                LocalDateTime.now(),
                errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }

    @ExceptionHandler(EmailUserDuplicationException.class)
    public ResponseEntity<ErrorResponse> handleEmailUserDuplicationException(
            EmailUserDuplicationException ex) {

        log.warn("Email duplicado {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Email já em uso",
                "O email inserido já está sendo usado por outro usuário",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {

        log.warn("User not found {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Usuário não encontrado",
                "Usuário com o ID informado não existente",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UserNotAuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleUserNotAuthorizationException(
            UserNotAuthorizationException ex) {

        log.warn("User not authorized {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Usuário não autorizado",
                "Usuário não tem permissáo para executar essa função",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(CpfExistsException.class)
    public ResponseEntity<ErrorResponse> handleCpfExistsException(CpfExistsException ex) {
        log.warn("Cpf existente {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("CPF em uso",
                "Já existe um usuário com esse numero de CPF cadastrado",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CpfAlreadyRegisterException.class)
    public ResponseEntity<ErrorResponse> handleCpfAlreadyRegisterException(CpfAlreadyRegisterException ex) {
        log.warn("Cpf existente {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("CPF já cadastrado",
                "Usuário já tem o CPF cadastrado",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NameCategoryExistException.class)
    public ResponseEntity<ErrorResponse> handleNameCategoryExistException(NameCategoryExistException ex) {

        log.warn("Name category existente {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Nome Já em uso",
                "Já existe uma outra categoria com esse nome em uso",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex){

        log.info("Categoria não encontrada {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Categoria não encontrada",
                "Categoria com o ID informada não foi encontrada",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex){

        log.info("Produto não encontrado {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Produto não encontrado",
                "O produto inserido não existe",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ProductNameExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductNameExistsException(ProductNameExistsException ex){

        log.info("Produto com nome já em uso {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Nome do produto já em uso",
                "O nome informado já está em uso em outro produto",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ProductAssociateWithOrdersException.class)
    public ResponseEntity<ErrorResponse> handleProductAssociateWithOrdersException(ProductAssociateWithOrdersException ex){

        log.info("Erro ao deletar produto pois existe pedidos desse produto {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Produto com pedido",
                "O produto que está sendo deletado possui pedido em andamento",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(StockOperationExceptionAddAndRemove.class)
    public ResponseEntity<ErrorResponse>handleStockOperationExceptionAddAndRemove(StockOperationExceptionAddAndRemove ex){

        log.warn("Erro de stock operation {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Erro ao adicionar ou retirar",
                "Erro ao adicionar ou remover item do estoque, por conta do saldo",
                LocalDateTime.now(),
                null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(StockOperationExceptionSet.class)
    public ResponseEntity<ErrorResponse>handleStockOperationExceptionSet(StockOperationExceptionSet ex){

        ErrorResponse error = new ErrorResponse(
                "Erro De Operação de estoque SET",
                "Erro ao tentar adicionar um valor negativo ao estoqque",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InsuficienteStockException.class)
    public ResponseEntity<ErrorResponse> handleInsuficienteStockException(InsuficienteStockException ex){

        log.info("Insuficiente estoque {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Estoqque insuficiente",
                "Estoqque insuficiente para realizar essa operação",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(OrderStatusCanceledException.class)
    public ResponseEntity<ErrorResponse>handleOrderStatusCanceledException(OrderStatusCanceledException ex){

        log.info("Erro ao cancelar o pedido {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Erro ao cancelar Pedido",
                "Não é possível cancelar um pedido que já foi enviado ou entregue",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(OrderStatusDeliveredException.class)
    public ResponseEntity<ErrorResponse>handleOrderStatusDeliveredException(OrderStatusDeliveredException ex){

        log.info("Erro ao entregar o pedido {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Erro ao entregar Pedido",
                "Pedido precisa ter sido ENVIADO para ser entregue.",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(OrderStatusShippedException.class)
    public ResponseEntity<ErrorResponse>handleOrderStatusShippedException(OrderStatusShippedException ex){

        log.info("Erro ao enviar o pedido {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Erro ao enviar Pedido",
                "Pedido precisa estar PAGO para ser enviado",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(OrderStatusPaidException.class)
    public ResponseEntity<ErrorResponse>handleOrderStatusPaidException(OrderStatusPaidException ex){

        log.info("Erro ao pagar o pedido {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Erro ao pagar Pedido",
                "Pedido só pode ser pago se estiver CRIADO.",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex){

        log.info("Erro ao tentar buscar o pedido {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Pedido não encontrado",
                "O pedido informado não existe",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAddressNotFoundException(AddressNotFoundException ex) {

        log.info("Address não encontrado {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Address não encontrado",
                "O address informado não foi encontrado",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex){

        log.info("Usuario tentou cancelar um pedido que não é dele");

        ErrorResponse error = new ErrorResponse("Pedido não pode ser cancelado",
                "Usuário tentou cancelar um pedido que não é seu",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PaymentStatusProcessingException.class)
    public ResponseEntity<ErrorResponse> handlePaymentStatusProcessingException(PaymentStatusProcessingException ex){

        log.info("Erro ao processar o pagamento {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Erro de processamento",
                "Houve um erro ao processar o pagamento",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    @ExceptionHandler(PaymentStatusCompleteException.class)
    public ResponseEntity<ErrorResponse> handlePaymentStatusCompleteException(PaymentStatusCompleteException ex){

        log.info("Erro ao completar o pagamento {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Erro ao completar o pagamento",
                "Houve um erro no momento de completar o pagamento",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PaymentStatusFailedException.class)
    public ResponseEntity<ErrorResponse> handlePaymentStatusFailedException(PaymentStatusFailedException ex){

        log.info("O pagamento falhou {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Pagamento falhou",
                "Falha no momento de concluir o pagamento",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PaymentStatusCancelledException.class)
    public ResponseEntity<ErrorResponse> handlePaymentStatusCancelledException(PaymentStatusCancelledException ex){

        log.info("Error ao cancelar o pagamento {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Erro de cancelamento",
                "Erro ao cancelar o pedido ou pagamento",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PaymentValueInsuficienteException.class)
    public ResponseEntity<ErrorResponse>handlePaymentValueInsuficienteException(PaymentValueInsuficienteException ex){

        log.info("Valor insuficiente {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Valor insuficiente",
                "Não foi possivel realizar o pagamento pois o valor é insuficiente",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex){

        log.info("Payment não encontrado {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Pagamento não encontrado",
                "O pagamento buscado não encontrado",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PaymentStatusRefundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentStatusRefundException(PaymentStatusRefundException ex){

        log.info("Erro ao realizar o estorno {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Erro de devolução",
                "Houve um erro no momento de fazer o estorno do valor pois o pagamento não foi completado",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotificationNotFoundException(NotificationNotFoundException ex){

        log.info("Notificação não encontrada {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Notificação não encontrada",
                "A notificação enviada não foi encontrada",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
