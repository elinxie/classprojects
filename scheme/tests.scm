;;; Test cases for Scheme.
;;;
;;; In order to run only a prefix of these examples, add the line
;;;
;;; (exit)
;;;
;;; after the last test you wish to run.

;;; **********************************
;;; *** Add more of your own here! ***
;;; **********************************

;;; These are examples from several sections of "The Structure
;;; and Interpretation of Computer Programs" by Abelson and Sussman.

;;; License: Creative Commons share alike with attribution

;;; 1.1.1

10
; expect 10

(+ 137 349)
; expect 486

(- 1000 334)
; expect 666

(* 5 99)
; expect 495

(/ 10 5)
; expect 2

(+ 2.7 10)
; expect 12.7

(+ 21 35 12 7)
; expect 75

(* 25 4 12)
; expect 1200

(+ (* 3 5) (- 10 6))
; expect 19

(+ (* 3 (+ (* 2 4) (+ 3 5))) (+ (- 10 7) 6))
; expect 57

(+ (* 3
      (+ (* 2 4)
         (+ 3 5)))
   (+ (- 10 7)
      6))
; expect 57




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Move the following (exit) line to run additional tests. ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; (exit)


;;; 1.1.2

(define size 2)
; expect size
size
; expect 2

(* 5 size)
; expect 10

(define pi 3.14159)
(define radius 10)
(* pi (* radius radius))
; expect 314.159

(define circumference (* 2 pi radius))
circumference
; expect 62.8318

;;; 1.1.4

(define (square x) (* x x))
; expect square
(square 21)
; expect 441

(define square (lambda (x) (* x x))) ; See Section 1.3.2
(square 21)
; expect 441

(square (+ 2 5))
; expect 49

(square (square 3))
; expect 81

(define (sum-of-squares x y)
  (+ (square x) (square y)))
(sum-of-squares 3 4)
; expect 25

(define (f a)
  (sum-of-squares (+ a 1) (* a 2)))
(f 5)
; expect 136

;;; 1.1.6

(define (abs x)
  (cond ((> x 0) x)
        ((= x 0) 0)
        ((< x 0) (- x))))
(abs -3)
; expect 3

(abs 0)
; expect 0

(abs 3)
; expect 3

(define (a-plus-abs-b a b)
  ((if (> b 0) + -) a b))
(a-plus-abs-b 3 -2)
; expect 5

;;; 1.1.7

(define (sqrt-iter guess x)
  (if (good-enough? guess x)
      guess
      (sqrt-iter (improve guess x)
                 x)))
(define (improve guess x)
  (average guess (/ x guess)))
(define (average x y)
  (/ (+ x y) 2))
(define (good-enough? guess x)
  (< (abs (- (square guess) x)) 0.001))
(define (sqrt x)
  (sqrt-iter 1.0 x))
(sqrt 9)
; expect 3.00009155413138

(sqrt (+ 100 37))
; expect 11.704699917758145

(sqrt (+ (sqrt 2) (sqrt 3)))
; expect 1.7739279023207892

(square (sqrt 1000))
; expect 1000.000369924366

;;; 1.1.8

(define (sqrt x)
  (define (good-enough? guess)
    (< (abs (- (square guess) x)) 0.001))
  (define (improve guess)
    (average guess (/ x guess)))
  (define (sqrt-iter guess)
    (if (good-enough? guess)
        guess
        (sqrt-iter (improve guess))))
  (sqrt-iter 1.0))
(sqrt 9)
; expect 3.00009155413138

(sqrt (+ 100 37))
; expect 11.704699917758145

(sqrt (+ (sqrt 2) (sqrt 3)))
; expect 1.7739279023207892

(square (sqrt 1000))
; expect 1000.000369924366

;;; 1.3.1

(define (cube x) (* x x x))
(define (sum term a next b)
  (if (> a b)
      0
      (+ (term a)
         (sum term (next a) next b))))
(define (inc n) (+ n 1))
(define (sum-cubes a b)
  (sum cube a inc b))
(sum-cubes 1 10)
; expect 3025

(define (identity x) x)
(define (sum-integers a b)
  (sum identity a inc b))
(sum-integers 1 10)
; expect 55

;;; 1.3.2

((lambda (x y z) (+ x y (square z))) 1 2 3)
; expect 12

(define (f x y)
  (let ((a (+ 1 (* x y)))
        (b (- 1 y)))
    (+ (* x (square a))
       (* y b)
       (* a b))))
(f 3 4)
; expect 456

(define x 5)
(+ (let ((x 3))
     (+ x (* x 10)))
   x)
; expect 38

(let ((x 3)
      (y (+ x 2)))
  (* x y))
; expect 21

;;; 2.1.1

(define (add-rat x y)
  (make-rat (+ (* (numer x) (denom y))
               (* (numer y) (denom x)))
            (* (denom x) (denom y))))
(define (sub-rat x y)
  (make-rat (- (* (numer x) (denom y))
               (* (numer y) (denom x)))
            (* (denom x) (denom y))))
(define (mul-rat x y)
  (make-rat (* (numer x) (numer y))
            (* (denom x) (denom y))))
(define (div-rat x y)
  (make-rat (* (numer x) (denom y))
            (* (denom x) (numer y))))
(define (equal-rat? x y)
  (= (* (numer x) (denom y))
     (* (numer y) (denom x))))

(define x (cons 1 2))
(car x)
; expect 1

(cdr x)
; expect 2

(define x (cons 1 2))
(define y (cons 3 4))
(define z (cons x y))
(car (car z))
; expect 1

(car (cdr z))
; expect 3

z
; expect ((1 . 2) 3 . 4)

(define (make-rat n d) (cons n d))
(define (numer x) (car x))
(define (denom x) (cdr x))
(define (print-rat x)
  (display (numer x))
  (display '/)
  (display (denom x))
  (newline))
(define one-half (make-rat 1 2))
(print-rat one-half)
; expect 1/2 ; okay

(define one-third (make-rat 1 3))
(print-rat (add-rat one-half one-third))
; expect 5/6 ; okay

(print-rat (mul-rat one-half one-third))
; expect 1/6 ; okay

(print-rat (add-rat one-third one-third))
; expect 6/9 ; okay

(define (gcd a b)
  (if (= b 0)
      a
      (gcd b (remainder a b))))
(define (make-rat n d)
  (let ((g (gcd n d)))
    (cons (/ n g) (/ d g))))
(print-rat (add-rat one-third one-third))
; expect 2/3 ; okay

(define one-through-four (list 1 2 3 4))
one-through-four
; expect (1 2 3 4)

(car one-through-four)
; expect 1

(cdr one-through-four)
; expect (2 3 4)

(car (cdr one-through-four))
; expect 2

(cons 10 one-through-four)
; expect (10 1 2 3 4)

(cons 5 one-through-four)
; expect (5 1 2 3 4)

(define (map proc items)
  (if (null? items)
      nil
      (cons (proc (car items))
            (map proc (cdr items)))))
(map abs (list -10 2.5 -11.6 17))
; expect (10 2.5 11.6 17)

(map (lambda (x) (* x x))
     (list 1 2 3 4))
; expect (1 4 9 16)

(define (scale-list items factor)
  (map (lambda (x) (* x factor))
       items))
(scale-list (list 1 2 3 4 5) 10)
; expect (10 20 30 40 50)

(define (count-leaves x)
  (cond ((null? x) 0)
        ((not (pair? x)) 1)
        (else (+ (count-leaves (car x))
                 (count-leaves (cdr x))))))
(define x (cons (list 1 2) (list 3 4)))
(count-leaves x)
; expect 4

(count-leaves (list x x))
; expect 8

;;; 2.2.3

(define (odd? x) (= 1 (remainder x 2)))
(define (filter predicate sequence)
  (cond ((null? sequence) nil)
        ((predicate (car sequence))
         (cons (car sequence)
               (filter predicate (cdr sequence))))
        (else (filter predicate (cdr sequence)))))
(filter odd? (list 1 2 3 4 5))
; expect (1 3 5)

(define (accumulate op initial sequence)
  (if (null? sequence)
      initial
      (op (car sequence)
          (accumulate op initial (cdr sequence)))))
(accumulate + 0 (list 1 2 3 4 5))
; expect 15

(accumulate * 1 (list 1 2 3 4 5))
; expect 120

(accumulate cons nil (list 1 2 3 4 5))
; expect (1 2 3 4 5)

(define (enumerate-interval low high)
  (if (> low high)
      nil
      (cons low (enumerate-interval (+ low 1) high))))
(enumerate-interval 2 7)
; expect (2 3 4 5 6 7)

(define (enumerate-tree tree)
  (cond ((null? tree) nil)
        ((not (pair? tree)) (list tree))
        (else (append (enumerate-tree (car tree))
                      (enumerate-tree (cdr tree))))))
(enumerate-tree (list 1 (list 2 (list 3 4)) 5))
; expect (1 2 3 4 5)

;;; 2.3.1

(define a 1)

(define b 2)

(list a b)
; expect (1 2)

(list 'a 'b)
; expect (a b)

(list 'a b)
; expect (a 2)

(car '(a b c))
; expect a

(cdr '(a b c))
; expect (b c)

(define (memq item x)
  (cond ((null? x) false)
        ((eq? item (car x)) x)
        (else (memq item (cdr x)))))
(memq 'apple '(pear banana prune))
; expect False

(memq 'apple '(x (apple sauce) y apple pear))
; expect (apple pear)

(define (equal? x y)
  (cond ((pair? x) (and (pair? y)
                        (equal? (car x) (car y))
                        (equal? (cdr x) (cdr y))))
        ((null? x) (null? y))
        (else (eq? x y))))
(equal? '(1 2 (three)) '(1 2 (three)))
; expect True

(equal? '(1 2 (three)) '(1 2 three))
; expect False

(equal? '(1 2 three) '(1 2 (three)))
; expect False

;;; Peter Norvig tests (http://norvig.com/lispy2.html)

(define double (lambda (x) (* 2 x)))
(double 5)
; expect 10

(define compose (lambda (f g) (lambda (x) (f (g x)))))
((compose list double) 5)
; expect (10)

(define apply-twice (lambda (f) (compose f f)))
((apply-twice double) 5)
; expect 20

((apply-twice (apply-twice double)) 5)
; expect 80

(define fact (lambda (n) (if (<= n 1) 1 (* n (fact (- n 1))))))
(fact 3)
; expect 6

(fact 50)
; expect 30414093201713378043612608166064768844377641568960512000000000000

(define (combine f)
  (lambda (x y)
    (if (null? x) nil
      (f (list (car x) (car y))
         ((combine f) (cdr x) (cdr y))))))
(define zip (combine cons))
(zip (list 1 2 3 4) (list 5 6 7 8))
; expect ((1 5) (2 6) (3 7) (4 8))

(define riff-shuffle (lambda (deck) (begin
    (define take (lambda (n seq) (if (<= n 0) (quote ()) (cons (car seq) (take (- n 1) (cdr seq))))))
    (define drop (lambda (n seq) (if (<= n 0) seq (drop (- n 1) (cdr seq)))))
    (define mid (lambda (seq) (/ (length seq) 2)))
    ((combine append) (take (mid deck) deck) (drop (mid deck) deck)))))
(riff-shuffle (list 1 2 3 4 5 6 7 8))
; expect (1 5 2 6 3 7 4 8)

((apply-twice riff-shuffle) (list 1 2 3 4 5 6 7 8))
; expect (1 3 5 7 2 4 6 8)

(riff-shuffle (riff-shuffle (riff-shuffle (list 1 2 3 4 5 6 7 8))))
; expect (1 2 3 4 5 6 7 8)

;;; Additional tests

(apply square '(2))
; expect 4

(apply + '(1 2 3 4))
; expect 10

(apply (if false + append) '((1 2) (3 4)))
; expect (1 2 3 4)

(if 0 1 2)
; expect 1

(if '() 1 2)
; expect 1

(or false true)
; expect True

(or)
; expect False

(and)
; expect True

(or 1 2 3)
; expect 1



(and 1 2 3)
; expect 3  


(and False (/ 1 0))
; expect False

(and True (/ 1 0))
; expect Error



(or False (/ 1 0))
; expect Error

(or 5 (/ 1 0))
; expect 5

(or (quote hello) (quote world))
; expect hello

(if nil 1 2)
; expect 1

(if 0 1 2)
; expect 1

(if (or false False #f) 1 2)
; expect 2

(define (loop) (loop))
(cond (false (loop))
      (12))
; expect 12

((lambda (x) (display x) (newline) x) 2)
; expect 2 ; 2

(define g (mu () x))
(define (high f x)
  (f))

(high g 2)
; expect 2

(define (print-and-square x)
  (print x)
  (square x))
(print-and-square 12)
; expect 12 ; 144

(/ 1 0)
; expect Error

(define addx (mu (x) (+ x y)))
(define add2xy (lambda (x y) (addx (+ x x))))
(add2xy 3 7)
; expect 13


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Scheme Implementations ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; len outputs the length of list s
(define (len s)
  (if (eq? s '())
    0
    (+ 1 (len (cdr s)))))
(len '(1 2 3 4))
; expect 4






;;;;;;;;;;;;;;;;;;;;
;;; Extra credit ;;;
;;;;;;;;;;;;;;;;;;;;


; Tail call optimization test
(define (sum n total)
  (if (zero? n) total
    (sum (- n 1) (+ n total))))
(sum 1001 0)
; expect 501501




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;Tests at the end;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

'guns

'"hello world"
'hello

'(1 . 2)

'(1 (2 three . (4 . 5)))

(car '(a b))

(eval (cons 'car '('(1 2))))

;;;;;
;;;;Question 3
;;;;;

(+ 2.2 3.3)
;expect 5.5

(- 4.24 7.56)
;expect -3.32

(* 8.5 2.5)
;expect 21.25

(/ 2.5 5)
;expect .5

(* (/ 2.5 5) 8 (+ 3 3))
;expect 24

(even? 5)
;expect False

;;;;;;;;;;;;
;;;;;Question 4
;;;;;;;;;;;

even?
;expect a function (don't know the specifics or what it will output)

hello
;expect unknown identifier

'hello
;expect hello

len
;expect a function (don't know the specifics or what it will output), this is a user defined function

;;;;;;;;;;;
;;;Question 5
;;;;;;;;;;;;


(define (x) 5)
;expect x

x
;expect 5

(define (d) x)
;expect d

d
;expect 5

(define (y) (/ (/ (* d x) x) d))
;expect y

y 
;expect 1

(define h +)
;expect h

(h 3 3)
;expect 6

;;;;;;;;;;;
;;;Question 6
;;;;;;;;;;

(cons 'eighty '(cons five eight (lambda nil)))
;expect ('eighty 'cons five eight (lambda nil))

'fart     '('lambda )
;expect fart ((quote lambda))

'guns
;expect guns

'"hello world"
;expect "hello world"

'hello
;expect hello

'(1 . 2)
;expect (1 . 2)

'(1 (2 three . (4 . 5)))
;expect (1 (2 three . (4 . 5)))

(car '(a b))

;expect a

(eval (cons 'car '('(1 2))))
;expect 1


;;;;;;;;;;;;
;;;;Question 7 
;;;;;;;;;;;;

(define x 5)
;expect x

(begin (define x 6) (define y 87) (define z (+ x y)) (/ z 5))
;expect 18.6

(begin ((lambda (x) (define y (+ x (* x x)))) 5 ) (+ y 5)) 
;expect 92

(begin ((lambda (x) (define y (+ x (* x x)))) 5 ))
;expect y

y
;expect 87

;;;;;;;;;;;;
;;;;Question 8
;;;;;;;;;;;;

;;tests above are tests for lambda too

((lambda (x) (begin (define y (+ x (* x x))) (+ y 5))) 5)
; expect 35


(define x (lambda (x y) (begin (define y (+ x (* x x))) (+ y 5))))
;expect x

(x 5 87)
;expect 35

;;;;;;;;;;;;
;;;Question 9 10 11 12 (because 9 10 11 and 12 build up on each other so you can create user defined functions)
;;;;;;;;;;;;

(define (tom) (lambda (eric linxie) (define cheng 5) (define cs61a (+ eric cheng)) (* cs61a linxie)))
;expect tom

(define freeborn (tom))
;expect freeborn

(freeborn 7 6)
;72

(define (tom) (lambda (eric linxie) (define cheng 5) (define (python cs lyfe) (+ lyfe (* cs (+ eric cheng)))) (* (python 2 6) linxie)))
;expect tom

(define foothill (tom))
;expect foothill

(foothill 7 6)
;180

;;;;;;;;;
;;;Question 13
;;;;;;;;;


(if 5 55 66)
;expect 55

(if 0 4 67)
;expect 4

(if nil 56 79)
;expect 56

(if 'cyote 'fun 60)
;expect fun

(if False 'eight "Nine")
;expect "Nine"

(if #f "Kanye" 'West)
;expect west


;;;;;;;;;;;;
;;;Question 14
;;;;;;;;;;;

(and)
;expect True
(or)
;expect False
(and 4 5 6)  
;expect 6
(or 5 2 1)  
;expect 5
(and #t #f 42 (/ 1 0))  
;expect False
(or 4 #t (/ 1 0))  
;expect 4

(or #f 0 Franklin)
;expect 0

(and True False Frank)
;expect False

(and True (define x 88) False)
;expect False

x
;expect 88

(or (define (x t) (+ t t) ) (define y 55))
;expect x

(x 5)
;expect 10

(or (define (y t) (* t t) ) (define x 55))
;expect y

(y 5)
;expect 25

(x 5)
;expect 10


;;;;;;;;;;;;
;;;;Question 15
;;;;;;;;;;;;

(cond ((= 4 3) 'nope)
           ((= 4 4) 'hi)
           (else 'wait))
;expect hi
(cond ((= 4 3) 'wat)
           ((= 4 4))
           (else 'hm))
;expect True
(cond ((= 4 4) 'here 42)
           (else 'wat 0))
;expect 42

(cond (12))
;expect 12
(cond ((= 4 3))
           ('hi))
;expect hi

(cond ('hi) ('else))
;expect hi

(cond (#f) ('gosh))
;gosh

;;;;;;;;;;;;;;
;;;;;;;Question 16
;;;;;;;;;;;;;

(define x 'hi)
;expect x
(define y 'bye)
;expect y
(let ((x 42) (y (* 5 10)))
       (list x y))
;expect (42 50)
(list x y)
;expect (hi bye)

(let ((x 42)) x 1 2)
;expect 2

(let ((albacore 5) (albatross (lambda (y) (+ 6 y))) (al "gore")) (define president (+ albacore 7)) (define josh (albatross 7)) al)
;expect "gore"

albatross
; expect Error: unknown identifier: albatross

;;;;;;;;;;;;;;;;;;
;;;;Question 17
;;;;;;;;;;;;;;;;;

(define f (mu (x) (+ x y)))
; expect f
(define g (lambda (x y) (f (+ x x))))
;expect g
(g 3 7)
;expect 13


(define (f y) (mu (z) (+ z y )))
;expect f

(define y 55)
;expect y

y
;expect 55

(define g (f 6))
;expect g

(let ((y 44)) (g 6))
;expect 50


;;;;;;;;;;Questions 18 19 and 20 on questions.scm 


;;;***************
;;;***Our Random Tests***
;;;***************

(* (- 83 536) (- 2 3))
; expect 453

(/ 3 5)
; expect 0.6

(+ (- 10 22) (/ 42 7))
; expect -6

(- -4 3)
; expect -7

;

'Batman
; expect batman

'(Bear on the Campanile)
; expect (bear on the campanile)

"Hey there little slice"
; expect "Hey there little slice"

(define midterms 'painful)
; expect midterms
(define student 'broke)
; expect student


(eval (car '(midterms student)))
; expect painful


(eval (car (cdr '(midterms student))))
;expect broke














