(* DOMINI SINTATTICI *)

type ide = string
type exp =
  (* Valori *)
  | Eint of int
  | Ebool of bool
  | Den of ide
  | Fun of ide * exp       (* Parametro, body*)

  (* Espressioni su interi *)
  | Sum of exp * exp       (* exp + exp *)
  | Sub of exp * exp       (* exp – exp *)
  | Mul of exp * exp       (* exp * exp *)
  | Eql of exp * exp       (* exp = exp *)
  | Lte of exp * exp       (* exp ≤ exp *)

  (* Espressioni su booleani *)
  | Orr of exp * exp       (* exp ∨ exp *)
  | And of exp * exp       (* exp ∧ exp *)
  | Not of exp             (*   ¬exp    *)

  (* Altre espressioni *)
  | Iff of exp * exp * exp (* if exp then exp else exp *)
  | Let of ide * exp * exp (* let ide = exp in exp *)
  | App of exp * exp       (* Call exp(exp) *)
  | Try of ide * exp * pat (* try ide with expr in pat *)

and pat =
  | CompClause of exp * exp * pat (* compound clause *)
  | BaseClause of exp * exp       (* E --> E *)
  | Wildcard of exp               (* _ --> E *)
