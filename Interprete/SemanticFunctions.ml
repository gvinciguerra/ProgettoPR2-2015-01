let evalArithmeticOperation op e1 e2 =
  match e1, e2 with
  | Int(x), Int(y) -> Int(op x y)
  | _ -> failwith "Both operands must be int"

let evalLogicalOperation op e1 e2 =
  match e1, e2 with
  | Bool(x), Bool(y) -> Bool(op x y)
  | _ -> failwith "Both operands must be bool"

let evalComparisonOperation op e1 e2 =
  match e1, e2 with
  | Int(x), Int(y) -> Bool(op x y)
  | _ -> failwith "Both operands must be int"

let evalNotOperation e =
  match e with
  | Bool(x) -> Bool(not x)
  | _ -> failwith "Operand must be bool"

let rec sem ((e:exp), (r:eval env)) =
  match e with
  | Eint(n) -> Int(n)
  | Ebool(b) -> Bool(b)
  | Den(i) -> applyenv(r,i)
  | Fun(i,bd) -> Funval(e,r)

  (* Espressioni su interi *)
  | Sum(a,b) -> evalArithmeticOperation (+) (sem(a,r)) (sem(b,r))
  | Sub(a,b) -> evalArithmeticOperation (-) (sem(a,r)) (sem(b,r))
  | Mul(a,b) -> evalArithmeticOperation ( * ) (sem(a,r)) (sem(b,r))
  | Eql(a,b) -> evalComparisonOperation (=) (sem(a,r)) (sem(b,r))
  | Lte(a,b) -> evalComparisonOperation (<=) (sem(a,r)) (sem(b,r))

  (* Espressioni su booleani *)
  | Orr(a,b) -> evalLogicalOperation (||) (sem(a,r)) (sem(b,r))
  | And(a,b) -> evalLogicalOperation (&&) (sem(a,r)) (sem(b,r))
  | Not(a) -> evalNotOperation (sem(a,r))

  (* Altre espressioni *)
  | Iff(c,t,e) ->
    let g = sem(c,r) in
    begin match g with
      | Bool(true) -> sem(t,r)
      | Bool(false) -> sem(e,r)
      | _ -> failwith "The condition must be bool"
    end
  | Let(i,e1,e2) -> sem(e2, bind(r,i,sem(e1, r)))
  | App(left,right) ->
    begin match sem(left,r) with
      | Funval(Fun(formparam,body),r2) -> sem(body, bind(r2,formparam,sem(right,r)))
      | _ -> failwith "This is not a function, it cannot be applied"
    end
  | Try(i,e,ptrn) ->
    let r2 = bind(r,i,sem(e,r)) in evalPattern ptrn r2

and evalPattern ptrn r =
  match ptrn with
  | CompClause(left,right,ptrn2) ->
    begin match sem(left,r) with
      | Bool(true) -> sem(right,r)
      | Bool(false) -> evalPattern ptrn2 r
      | _ -> failwith "The pattern must be bool"
    end
  | BaseClause(left,right) ->
    begin match sem(left,r) with
      | Bool(true) -> sem(right,r)
      | Bool(false) -> failwith "Match failed"
      | _ -> failwith "The pattern must be bool"
    end
  | Wildcard(right) -> sem(right,r)
