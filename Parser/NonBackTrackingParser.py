# push $ onto stack;
# push start symbol S onto stack;
# let a be the first symbol of input;
# let T be the top stack symbol;
# while (T ≠ $) {
#  if (T = a)
#  { pop stack;
#  let a be next symbol of input}
#  elseif (T is a terminal) {error()}
#  elseif (M[T,a] is empty) {error()}
#  elseif (M[T,a]=ʻT::=U1U2..Ukʼ)
#  { output the production ʻT::=U1U2..Ukʼ;
#  pop stack;
#  push Uk, ..U2, U1 onto stack}
#  let T be the top stack symbol}