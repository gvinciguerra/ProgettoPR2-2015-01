#use "SyntacticDomains.ml";;
#use "SemanticDomains.ml";;
#use "SemanticFunctions.ml";;

(*
  Programma di test per l'interprete
  Giorgio Vinciguerra - 2 giugno 2015
  #trace sem;; per il debug
*)

let anEmptyEnv = emptyenv Unbound;;

(********** ESPRESSIONI ELEMENTARI SU INTERI E BOOLEANI **********)

let e = Let("i", Eint 111111, Mul(Den "i", Den "i"));;
assert (Int 12345654321 = sem(e, anEmptyEnv));;
print_endline "Test 0 superato";;

let e = Let("i", Ebool false , Not(Den "i"));;
assert (Bool true = sem(e, anEmptyEnv));;
print_endline "Test 1 superato";;

let e = Let("i", Ebool true, And(Den "i", Not(Den "i")));;
assert (Bool false = sem(e, anEmptyEnv));;
print_endline "Test 2 superato";;

let e = Let("a", Ebool true, Let("b", Ebool false, Orr(Den "a", Den "b")));;
assert (Bool true = sem(e, anEmptyEnv));;
print_endline "Test 3 superato";;

(********** FUNZIONI (è riportata anche la sintassi concreta) **********)

let sign = (* let sign = fun n -> if n = 0 then 0 else if n <= 0 then -1 else 1 in sign 4 *)
  Let("sign",
    Fun("n",
      Iff(Eql(Den "n", Eint 0),
        Eint 0,
        Iff(Lte(Den "n", Eint 0), Eint (-1), Eint 1))),
    App(Den "sign", Eint 4));;
assert (Int 1 = sem(sign, anEmptyEnv));;
print_endline "Test 4 superato";;

let add3 = (* let add3 = fun n -> 3 + n in add3 2012 *)
  Let("add3",
    Fun("n", Sum(Eint 3, Den "n")),
    App(Den "add3", Eint 2012));;
assert (Int 2015 = sem(add3, anEmptyEnv));;
print_endline "Test 5 superato";;

let dumbChangeSign = (* let changeSign = fun n -> try x with n (n<=0 --> -1*n) :: (0<=n --> -1*n) in changeSign 1123 *)
  Let("changeSign",
    Fun("n",
      Try("x",
        Den "n",
        CompClause(Lte(Den "x", Eint 0), Mul(Eint(-1), Den "x"),
          BaseClause(Lte(Eint 0, Den "x"), Mul(Eint(-1), Den "x"))
        ))),
    App(Den "changeSign", Eint 1123)
  );;
assert (Int(-1123) = sem(dumbChangeSign, anEmptyEnv));;
print_endline "Test 6 superato";;

let mustFail = (* try x with 4–3 in (x ≤ 0 --> false) :: (x == 0 --> true) *)
  Try ("x",
    Sub(Eint 4, Eint 3),
    CompClause(Lte(Den "x", Eint 0), Ebool false,
      BaseClause(Eql(Eint 0, Den "x"), Ebool true)
      ));;
try let _ = sem(mustFail, anEmptyEnv) in raise Exit
with
| Failure _ -> print_endline "Test 7 superato"
| _ -> failwith "Il test 7 doveva sollevare una Failure";;

let isZeroOrOne = (* try x with 10 in (x == 0 --> true) :: (x == 1 --> true) :: (_ --> false) *)
  Try ("x",
    Eint 10,
      CompClause(Eql(Den "x", Eint 0), Ebool true,
        CompClause(Eql(Den "x", Eint 1), Ebool true, Wildcard(Ebool false))));;
assert (Bool false = sem(isZeroOrOne, anEmptyEnv));;
print_endline "Test 8 superato";;

(* let sqrOrCb x =
     let sqr x = x*x in
        let cb x = x*x*x in
          try x with x in (x >= 0 --> cb x) :: (x <= 0 --> sqr x)
 *)
let squareIfNegativeCubeOtherwise (inval:exp) =
  Let("sqrOrCb",
    Fun("x",
      Let("sqr", Fun("x", Mul(Den "x", Den "x")),
        Let("cb", Fun("x", Mul(Den "x", Mul(Den "x", Den "x"))),
          Try("x",
            Den "x",
            CompClause(
                Lte(Den "x", Eint 0), App(Den "sqr", Den "x"),
                BaseClause(Lte(Eint 0, Den "x"), App(Den "cb", Den "x"))
            ))))),
    App(Den "sqrOrCb", inval)
  );;
assert (Int 25 = sem(squareIfNegativeCubeOtherwise (Eint (-5)), anEmptyEnv));;
print_endline "Test 9 superato";;

assert (Int 1000 = sem(squareIfNegativeCubeOtherwise (Eint 10), anEmptyEnv));;
print_endline "Test 10 superato";;
