(* DOMINI SEMANTICI ELEMENTARI *)
type eval =
  | Unbound
  | Int of int
  | Bool of bool
  | Funval of efun
and efun = exp * (eval env)

(* DOMINI SEMANTICI DERIVATI *)
and 't env = ide -> 't

let emptyenv x = function y -> x
let applyenv (x, y) = x y
let bind ((r: eval env), (l:ide), (e:eval)) lu =
  if lu = l then e else applyenv (r, lu)
