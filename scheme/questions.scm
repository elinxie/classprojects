; Some utility functions that you may find useful.
(define (apply-to-all proc items)
  (if (null? items)
      '()
      (cons (proc (car items))
            (apply-to-all proc (cdr items)))))

(define (keep-if predicate sequence)
  (cond ((null? sequence) nil)
        ((predicate (car sequence))
         (cons (car sequence)
               (keep-if predicate (cdr sequence))))
        (else (keep-if predicate (cdr sequence)))))

(define (accumulate op initial sequence)
  (if (null? sequence)
      initial
      (op (car sequence)
          (accumulate op initial (cdr sequence)))))

(define (caar x) (car (car x)))
(define (cadr x) (car (cdr x)))
(define (cddr x) (cdr (cdr x)))
(define (cadar x) (car (cdr (car x))))

; Problem 18
;; Turns a list of pairs into a pair of lists

(define (zip list_of_lists)
  (define (zip_help list_of_lists new1 new2)
    (if (null? list_of_lists) (cons (reverse new1) (cons (reverse new2) nil))
    (zip_help (cdr list_of_lists) (cons (car (car list_of_lists)) new1) (cons (car (cdr (car list_of_lists))) new2))
       )

    )
 
    (define (reverse lst)
    (if (null? lst ) nil

      (append (reverse (cdr lst)) (cons (car lst) nil))

      )
    )
  
    (zip_help list_of_lists nil nil)

  
  
  )

(zip '())
; expect (() ())
(zip '((1 2)))
; expect ((1) (2))
(zip '((1 2) (3 4) (5 6)))
; expect ((1 3 5) (2 4 6))

; Problem 19

;; A list of all ways to partition TOTAL, where  each partition must
;; be at most MAX-VALUE and there are at most MAX-PIECES partitions.
; (define (list-partitions total max-pieces max-value)
;   'YOUR-CODE-HERE

(define (append v s) 
(if (null? v) s

  (cons (car v) (append (cdr v) s))


  )



  )




  (define (list-partitions total max-pieces max-value)
    (cond 

      ; ((and (= max-pieces 1) (< total max-value)) total)
      ((= total 0) (cons '() nil) )
      ((= max-pieces 0)  '())
      ((= max-value 0) '())
      

      ( (>= total max-value) (append (apply-to-all (lambda (partition) (cons max-value partition)) 
                                    (list-partitions (- total max-value) (- max-pieces 1) max-value)) 
        (list-partitions total max-pieces (- max-value 1))))


      ((< total max-value) (list-partitions total max-pieces (- max-value 1)))
      

      )




    )
  ; (partition total max-pieces max-value)

  ; )


(list-partitions 5 2 4)
; expects a permutation of ((4 1) (3 2))
(list-partitions 7 3 5)
; expects a permutation of ((5 2) (5 1 1) (4 3) (4 2 1) (3 3 1) (3 2 2))


; Problem 20
;; Returns a function that takes in an expression and checks if it is the special
;; form FORM
(define (check-special form)
  (lambda (expr) (equal? form (car expr))))

(define lambda? (check-special 'lambda))
(define define? (check-special 'define))
(define quoted? (check-special 'quote))
(define let?    (check-special 'let))

;; Converts all let special forms in EXPR into equivalent forms using lambda



(define (analyze expr)
  (cond ((atom? expr)
         ;YOUR-CODE-HERE
         expr
         )
        ((quoted? expr)
         ;YOUR-CODE-HERE
         expr
         ; (cons 'quote (analyze (cdr expr)))
         )
        ((or (lambda? expr)
             (define? expr))
         (let ((form   (car expr))
               (params (cadr expr))
               (body   (cddr expr)))
           ;YOUR-CODE-HERE
           (cons form (cons params  (apply-to-all analyze body) ))

           

           ))
        ((let? expr)
         (let ((values (cadr expr))
               (body   (cddr expr)))
           ;YOUR-CODE-HERE
           ; (cons (cons 'lambda (cons (car (zip values)) (analyze body)) (car (cdr (zip values))))
            (append (cons (cons 'lambda (cons (car (zip values)) (apply-to-all analyze body))) nil) (apply-to-all analyze (car (cdr (zip values)))) )
           )
         )
        (else
         ;YOUR-CODE-HERE
         (apply-to-all analyze expr)
          )))
  
  

(analyze 1)
; expect 1
(analyze 'a)
; expect a
(analyze '(+ 1 2))
; expect (+ 1 2)

;; Quoted expressions remain the same
(analyze '(quote (let ((a 1) (b 2)) (+ a b))))
; expect (quote (let ((a 1) (b 2)) (+ a b)))

;; Lambda parameters not affected, but body affected
(analyze '(lambda (let a b) (+ let a b)))
; expect (lambda (let a b) (+ let a b))
(analyze '(lambda (x) a (let ((a x)) a)))
; expect (lambda (x) a ((lambda (a) a) x))

(analyze '(let ((a 1)
                (b 2))
            (+ a b)))
; expect ((lambda (a b) (+ a b)) 1 2)
(analyze '(let ((a (let ((a 2)) a))
                (b 2))
            (+ a b)))
; expect ((lambda (a b) (+ a b)) ((lambda (a) a) 2) 2)
(analyze '(let ((a 1))
            (let ((b a))
              b)))
; expect ((lambda (a) ((lambda (b) b) a)) 1)


;; Problem 21 (optional)
;; Draw the hax image using turtle graphics.
(define (hax d k)
  'YOUR-CODE-HERE
  nil)


;;;;;;;;;;;;;;;
;;;;;;Question 18
;;;;;;;;;;;;;;;

(zip '((b "list") (fun "time") (eighties (define garb "eight"))))
;expect ((bogus fun eighties) ("list" "time" (define garb "eight")))



;;;;;;;;;;;;;
;;;;;;Question 19
;;;;;;;;;;;;

(list-partitions 0 1 2)
;expect (())


(list-partitions 1 0 3)
;expect ()

(list-partitions 1 1 0)
;expect ()

;;;;;;;;;;;;;;
;;;;;;Question 20
;;;;;;;;;;;;;;

(analyze '(let ((x 42) (y (* 5 10)))
       (list x y)))
;expect ((lambda (x y) (list x y)) 42 50)


(analyze '(let ((x 42)) x 1 2))
;expect ((lambda (x) x 1 2) 42 )



