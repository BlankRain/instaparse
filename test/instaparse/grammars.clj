(ns instaparse.grammars
  (:use clojure.test)
  (:use instaparse.combinators instaparse.reduction)
  (:require [instaparse.gll :as gll]))

(defn- parse [grammar start text]
  (gll/parse (apply-standard-reductions grammar) start text false))

(defn- parses [grammar start text]
  (gll/parses (apply-standard-reductions grammar) start text false))

;; Grammars built with combinators

(def grammar1 {:s (alt (string "a") (string "aa") (string "aaa"))})
(def grammar2 {:s (alt (string "a") (string "b"))})
(def grammar3 {:s (alt (cat (string "a") (nt :s)) Epsilon)})
(def grammar4 {:y (string "b")
               :x (cat (string "a") (nt :y))})            
(def grammar5 {:s (cat (string "a") (string "b") (string "c"))})
(def grammar6 {:s (alt (cat (string "a") (nt :s)) (string "a"))})
(def grammar7 {:s (alt (cat (string "a") (nt :s)) Epsilon)})
(def grammar8 {:s (alt (cat (string "a") (nt :s) Epsilon) (string "a"))})
(def grammar9 {:s (alt (cat (string "a") (nt :s))
                       (cat (string "b") (nt :s))
                       Epsilon)})
(def grammar10 {:s (alt (cat (nt :s) (string "a") )
                       (cat (nt :s) (string "b") )
                       Epsilon)})
(def grammar11 {:s (alt (cat (nt :s) (string "a")) (string "a"))})
(def grammar12 {:s (alt (nt :a) (nt :a) (nt :a))
                :a (alt (cat (nt :s) (string "a")) (string "a"))})
(def grammar13 {:s (nt :a)
                :a (alt (cat (nt :s) (string "a")) (string "a"))})
(def amb-grammar {:s (alt (string "b") 
                          (cat (nt :s) (nt :s))
                          (cat (nt :s) (nt :s) (nt :s)))})
(def paren-grammar {:s (alt (cat (string "(") (string ")"))
                            (cat (string "(") (nt :s) (string ")"))
                            (cat (nt :s) (nt :s)))})
(def non-ll-grammar {:s (alt (nt :a) (nt :b))
                      :a (alt (cat (string "a") (nt :a) (string "b"))
                              Epsilon)
                      :b (alt (cat (string "a") (nt :b) (string "bb"))
                              Epsilon)})
(def grammar14 {:s (cat (opt (string "a")) (string "b"))})
(def grammar15 {:s (cat (opt (string "a")) (opt (string "b")))})
(def grammar16 {:s (plus (string "a"))})
(def grammar17 {:s (cat (plus (string "a")) (string "b"))})
(def grammar18 {:s (cat (plus (string "a")) (string "a"))})
(def grammar19 {:s (cat (string "a") (plus (alt (string "b")
                                                (string "c"))))})
(def grammar20 {:s (cat (string "a") (plus (cat (string "b")
                                                (string "c"))))})
(def grammar21 {:s (cat (string "a") (plus (alt (string "b")
                                                (string "c")))
                        (string "b"))})
(def grammar22 {:s (star (string "a"))})
(def grammar23 {:s (cat (star (string "a")) (string "b"))})
(def grammar24 {:s (cat (star (string "a")) (string "a"))})
(def grammar25 {:s (cat (string "a") (star (alt (string "b")
                                                (string "c"))))})
(def grammar26 {:s (cat (string "a") (star (cat (string "b")
                                                (string "c"))))})
(def grammar27 {:s (cat (string "a") (star (alt (string "b")
                                                (string "c")))
                        (string "b"))})
(def grammar28 {:s (regexp "a[0-9]b+c")})
(def grammar29 {:s (plus (opt (string "a")))})
(def paren-grammar 
  {:a (red (cat (string "(") (opt (nt :a)) (string ")"))
           (fn ([_ _] ())
             ([_ l _] (list l))))})
(def grammar30 {:s (alt (nt :a) (nt :b))
                :a (plus (cat (string "a") (string "b")))
                :b (plus (cat (string "a") (string "b")))})
;equal: [zero one | one zero]   ;; equal number of "0"s and "1"s.
;
;zero: "0" equal | equal "0"    ;; has an extra "0" in it.
;
;one: "1" equal | equal "1"     ;; has an extra "1" in it.
(def equal-zeros-ones {:equal (opt (alt (cat (nt :zero) (nt :one))
                                        (cat (nt :one) (nt :zero))))
                       :zero (alt (cat (string "0") (nt :equal))
                                  (cat (nt :equal) (string "0")))
                       :one (alt (cat (string "1") (nt :equal))
                                 (cat (nt :equal) (string "1")))})
(def grammar31 {:equal (alt (cat (string "0") (nt :equal) (string "1"))
                            (cat (string "1") (nt :equal) (string "0"))
                            (cat (nt :equal) (nt :equal))
                            Epsilon)})
; Another slow one
(def grammar32 {:s (alt (string "0")
                        (cat (nt :s) (nt :s))
                        Epsilon)})

(def grammar33 {:s (alt (cat (nt :s) (nt :s))
                        Epsilon)})
(def grammar34 {:s (alt (nt :s) Epsilon)})
(def grammar35 {:s (opt (cat (nt :s) (nt :s)))})
(def grammar36 {:s (cat (opt (nt :s)) (nt :s))})
(def grammar37 {:s (cat (nt :s) (opt (nt :s)))})
(def grammar38 {:s (regexp "a[0-9](bc)+")})
(def grammar39 {:s (cat (string "0") (hide (string "1"))(string "2"))})
(def grammar40 {:s (nt :aa)
                :aa (hide-tag (alt Epsilon (cat (string "a") (nt :aa))))})
(def grammar41 {:s (cat (string "b") (plus (string "a")))})
(def grammar42 {:s (cat (string "b") (star (string "a")))})
(def grammar43 {:s (cat (star (string "a")) (string "b"))})
(def grammar44 {:s (cat (look (string "ab")) (nt :ab))
                :ab (plus (alt (string "a") (string "b")))})
(def grammar45 {:s (cat (nt :ab) (look (string "ab")))
                :ab (plus (alt (string "a") (string "b")))})

(def grammar46 {:s (cat (nt :ab) (look Epsilon))
                :ab (plus (alt (string "a") (string "b")))})
(def grammar47 {:s (cat (neg (string "ab")) (nt :ab))
                :ab (plus (alt (string "a") (string "b")))})
(def grammar48 {:s (cat (nt :ab) (neg (string "ab")))
                :ab (plus (alt (string "a") (string "b")))})
(def grammar49 {:s (cat (nt :ab) (neg Epsilon))
                :ab (plus (alt (string "a") (string "b")))})
; Grammar for odd number of a's.  
(def grammar50 {:s (alt (cat (string "a") (nt :s) (string "a"))
                        (string "a"))})
(def grammar51 {:s (hide-tag (alt (cat (string "a") (nt :s) (string "a"))
                                  (string "a")))})
(def grammar52 {:s (hide-tag (alt (cat (string "a") (nt :s) (string "b"))
                                  (string "a")))})
(def grammar53 {:s (hide-tag (alt (cat (string "a") (nt :s) (string "a"))
                                  (string "b")))})
(def grammar54 {:s (cat (string "a")
                        (star (string "aa")))})
(def grammar55 {:s (alt (cat (string "a") (nt :s) (opt (string "a")))
                        (string "a"))})
(def grammar56 {:s (alt (string "a")
                        (cat (string "a") (nt :s) (string "a"))
                        )})
;; PEG grammars
(def grammar57 {:s (ord (plus (string "aa"))
                        (plus (string "a")))})

(def grammar58 {:s (cat (ord (plus (string "aa"))
                             (plus (string "a")))
                        (string "b"))})

(def grammar59 {:S (cat (look (cat (nt :A) (string "c")))
                        (plus (string "a"))
                        (nt :B)
                        (neg (ord (string "a") (string "b") (string "c"))))
                :A (cat (string "a") (opt (nt :A)) (string "b"))
                :B (hide-tag (cat (string "b") (opt (nt :B)) (string "c")))}) 

(def grammar60 {:Expr (ord (nt :Product) (nt :Sum) (nt :Value))
                :Product (cat (nt :Expr) 
                              (star (cat (alt (string "*")
                                              (string "/"))
                                         (nt :Expr))))
                :Sum (cat (nt :Expr)
                          (star (cat (alt (string "+")
                                          (string "-"))
                                     (nt :Expr))))
                :Value (alt (regexp "[0-9]+")
                            (cat (string "(")
                                 (nt :Expr)
                                 (string ")")))})
                            

(def grammar61 {:Expr (alt (nt :Product) (nt :Value))
                :Product (cat (nt :Expr) 
                              (star (cat (alt (string "*")
                                              (string "/"))
                                         (nt :Expr))))                
                :Value (alt (string "[0-9]+")
                            (cat (string "(")
                                 (nt :Expr)
                                 (string ")")))})

(def grammar62 {:Expr (alt (nt :Product) (string "0"))
                :Product (plus (nt :Expr))}) 
                
(def grammar63 {:Expr (alt (nt :Expr) (string "0"))})
(def grammar64 {:Expr (hide-tag (alt (nt :Product) 
                                     (cat (neg (nt :Product)) (nt :Sum))
                                     (cat (neg (nt :Product))
                                          (neg (nt :Sum))
                                          (nt :Value))))
                :Product (cat (nt :Expr) 
                              (star (cat (alt (string "*")
                                              (string "/"))
                                         (nt :Expr))))
                :Sum (cat (nt :Expr)
                          (star (cat (alt (string "+")
                                          (string "-"))
                                     (nt :Expr))))
                :Value (alt (regexp "[0-9]+")
                            (cat (string "(")
                                 (nt :Expr)
                                 (string ")")))})

(def grammar65 {:s (cat (alt (plus (string "aa"))
                             (cat 
                               (neg (plus (string "aa")))
                               (plus (string "a"))))
                        (string "b"))})

(def grammar66 {:s (neg (nt :s))})
(def grammar67 {:s (cat (neg (nt :s)) (string "0"))})
(def grammar68 {:s (cat (neg (nt :a)) (string "0"))
                :a (neg (nt :s))})
(def grammar69 {:s (cat (neg (nt :a)) (string "abc"))
                :a (cat (neg (string "b")) (string "c"))})
(def grammar70 {:s (cat (neg (nt :a)) (string "abc"))
                :a (cat (neg (string "b")) (string "a"))})