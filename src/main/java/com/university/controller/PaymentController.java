package com.university.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.models.Enrollment;
import com.university.models.Payment;
import com.university.repository.EnrollmentRepository;
import com.university.repository.PaymentRepository;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class PaymentController {
	@Autowired
	private EnrollmentRepository enrollmentRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	//Get all payments
	@GetMapping("/payments")
	public List<Payment> listPayments(){
		return paymentRepository.findAll();
	}
    
	//Get a payment by paymentId
    @GetMapping("/payments/{paymentId}")
   	public ResponseEntity<Payment> getPaymentById(@PathVariable Long paymentId){
    	Payment payment = paymentRepository.findById(paymentId).
   		orElseThrow(()-> new ResourceNotFoundException("The Payment with id "+ paymentId +" doesn't exit in the database !!"));
   		
   		return new ResponseEntity<>(payment,HttpStatus.OK);
   		
   	}
    
    //Add a payment using enrollmentId
    @PostMapping("/enrollments/{enrollmentId}/payments")
    public Payment createPayment(@PathVariable  Long enrollmentId,
                                 @Valid @RequestBody Payment payment) {
        return enrollmentRepository.findById(enrollmentId).map(enrollment -> {
           payment.setEnrollment(enrollment);
            return paymentRepository.save(payment);
        }).orElseThrow(() -> new ResourceNotFoundException("EnrollmentId " + enrollmentId + " not found"));
    }
    
    //Update a payment using enrollmentId
    @PutMapping("/enrollments/{enrollmentId}/payments/{paymentId}")
    public Payment updatePayment(@PathVariable  Long enrollmentId,
                                 @PathVariable  Long paymentId,
                                 @Valid @RequestBody Payment paymentRequest) {
        if(!enrollmentRepository.existsById(enrollmentId)) {
            throw new ResourceNotFoundException("EnrollmentId " + enrollmentId + " not found");
        }
        return paymentRepository.findById(paymentId).map(payment -> {
        	payment.setPaymentDate(paymentRequest.getPaymentDate());
        	payment.setDescription(paymentRequest.getDescription());
        	payment.setAmount(paymentRequest.getAmount());
        	payment.setTransactionNumber(paymentRequest.getTransactionNumber());
            return paymentRepository.save(payment);
        }).orElseThrow(() -> new ResourceNotFoundException("PaymentId " + paymentId + "not found"));
    }
    
    //Patch a payment property 
    @PatchMapping("/enrollments/{enrollmentId}/payments/{paymentId}")
    public ResponseEntity<Payment> patchPayment(@PathVariable Long enrollmentId,
                                 @PathVariable  Long paymentId,
                                 @RequestBody 	Payment patch) {
    	Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentId);
        if (!optionalEnrollment.isPresent()) {
            throw new ResourceNotFoundException("EnrollmentId " + enrollmentId + " not found");
        }
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (!optionalPayment.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Payment payment = optionalPayment.get();
        payment = applyPatch(payment, patch);
        payment = paymentRepository.save(payment);
        return ResponseEntity.ok(payment);
      }

    private Payment applyPatch(Payment payment, Payment patch) {
        if (patch.getPaymentDate() != null) {
        	payment.setPaymentDate(patch.getPaymentDate());
        }
        if (patch.getDescription() != null) {
        	payment.setDescription(patch.getDescription());
        }
        if (patch.getAmount() != 0.0) {
        	payment.setAmount(patch.getAmount());
        }
        if (patch.getTransactionNumber() != null) {
        	payment.setTransactionNumber(patch.getTransactionNumber());
        }
        
        return payment;
    }


    
	
    //Delete a payment using enrollmentId
    @DeleteMapping("enrollments/{enrollmentId}/payments/{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable (value = "studentId") Long enrollmentId,
                              @PathVariable Long paymentId) {
        return paymentRepository.findByIdAndEnrollmentId(paymentId, enrollmentId).map(payment -> {
            paymentRepository.delete(payment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + paymentId + " and enrollmentId " + enrollmentId));
    }

}
