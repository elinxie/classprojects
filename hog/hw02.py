# CS 61A Fall 2014
# Name: Eric Linxie
# Login: cs61a.bik@cory.eecs.berkeley.edu

def square(x):
   return x * x

def triple(x):
   return 3 * x

def identity(x):
   return x

def increment(x):
   return x + 1

def piecewise(f, g, b):
   def new_piece(x):
      if x < b:
         return f(x)
      else:
         return g(x)
   return new_piece

   """Returns the piecewise function h where:

   h(x) = f(x) if x < b,
          g(x) otherwise

   >>> def negate(x):
   ...     return -x
   >>> abs_value = piecewise(negate, identity, 0)
   >>> abs_value(6)
   6
   >>> abs_value(-1)
   1
   """
   "*** YOUR CODE HERE ***"

def intersects(f, x):
   """Returns a function that returns whether f intersects g at x.

   >>> at_three = intersects(square, 3)
   >>> at_three(triple) # triple(3) == square(3)
   True
   >>> at_three(increment)
   False
   >>> at_one = intersects(identity, 1)
   >>> at_one(square)
   True
   >>> at_one(triple)
   False
   """
   def test_for_intersect(g):
      if f(x)==g(x):
         return True
      else:
         return False
   return test_for_intersect


   >>> add_three = repeated(increment, 3)
   >>> add_three(5)
   8
   >>> repeated(triple, 5)(1) # 3 * 3 * 3 * 3 * 3 * 1
   243
   >>> repeated(square, 2)(5) # square(square(5))
   625
   >>> repeated(square, 4)(5) # square(square(square(square(5))))
   152587890625
   
   def repeated_function(x):
      counter=0
      value=x
      while counter<n:
         value =f(value)
         counter+=1
      return value 
   return repeated_function

