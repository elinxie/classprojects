"""The Game of Hog."""

from dice import four_sided, six_sided, make_test_dice
from ucb import main, trace, log_current_line, interact

GOAL_SCORE = 100 # The goal of Hog is to score 100 points.

######################
# Phase 1: Simulator #
######################

def roll_dice(num_rolls, dice=six_sided):
    """Roll DICE for NUM_ROLLS times.  Return either the sum of the outcomes,
    or 1 if a 1 is rolled (Pig out). This calls DICE exactly NUM_ROLLS times.

    num_rolls:  The number of dice rolls that will be made; at least 1.
    dice:       A zero-argument function that returns an integer outcome.
    """
    # These assert statements ensure that num_rolls is a positive integer.
    assert type(num_rolls) == int, 'num_rolls must be an integer.'
    assert num_rolls > 0, 'Must roll at least once.'
    rolling = 1
    sum_numbers = 0
    while rolling <= num_rolls:
        number = dice()
        if num_rolls == 1:
            return dice()
        elif number == 1:
            return 1
        else:
            sum_numbers = sum_numbers + number
        rolling += 1
    return sum_numbers

def free_bacon(opponent_score):
    return abs(opponent_score // 10 - opponent_score % 10) + 1

def take_turn(num_rolls, opponent_score, dice=six_sided):
    """Simulate a turn rolling NUM_ROLLS dice, which may be 0 (free bacon).

    num_rolls:       The number of dice rolls that will be made.
    opponent_score:  The total score of the opponent.
    dice:            A function of no args that returns an integer outcome.
    """
    assert type(num_rolls) == int, 'num_rolls must be an integer.'
    assert num_rolls >= 0, 'Cannot roll a negative number of dice.'
    assert num_rolls <= 10, 'Cannot roll more than 10 dice.'
    assert opponent_score < 100, 'The game should be over.'

    if num_rolls > 0:
        return roll_dice(num_rolls, dice)
    else:
        return free_bacon(opponent_score)

def select_dice(score, opponent_score):
    """Select six-sided dice unless the sum of SCORE and OPPONENT_SCORE is a
    multiple of 7, in which case select four-sided dice (Hog wild).
    """
    if (score + opponent_score) % 7 == 0:
        return four_sided
    else:
        return six_sided

def bid_for_start(bid0, bid1, goal=GOAL_SCORE):
    """Given the bids BID0 and BID1 of each player, returns three values:

    - the starting score of player 0
    - the starting score of player 1
    - the number of the player who rolls first (0 or 1)
    """
    assert bid0 >= 0 and bid1 >= 0, "Bids should be non-negative!"
    assert type(bid0) == int and type(bid1) == int, "Bids should be integers!"

    # The buggy code is below:
    if bid0 == bid1:
        return goal, goal, 0 or 1
    if bid0 == bid1 - 5:
        return 0, 10, 1
    if bid1 == bid0 - 5:
        return 10, 0, 0
    if bid1 > bid0:
        return bid1, bid0, 1
    else:
        return bid1, bid0, 0

def other(who):
    """Return the other player, for a player WHO numbered 0 or 1.

    >>> other(0)
    1
    >>> other(1)
    0
    """
    return 1 - who

def swine_swap(score, opponent_score):
    if score * 2 == opponent_score or opponent_score * 2 == score:
        score, opponent_score = opponent_score, score
        return score, opponent_score
    else:
        return score, opponent_score

def play(strategy0, strategy1, score0=0, score1=0, goal=GOAL_SCORE):
    """Simulate a game and return the final scores of both players, with
    Player 0's score first, and Player 1's score second.

    A strategy is a function that takes two total scores as arguments
    (the current player's score, and the opponent's score), and returns a
    number of dice that the current player will roll this turn.

    strategy0:  The strategy function for Player 0, who plays first
    strategy1:  The strategy function for Player 1, who plays second
    score0   :  The starting score for Player 0
    score1   :  The starting score for Player 1
    """
    dice = four_sided
    who = 0  
    while score0 < goal and score1 < goal:
        if who == 0:
            score0 = take_turn(strategy0(score0, score1), score1, dice) + score0
            dice = select_dice(score0, score1)
            score0, score1 = swine_swap(score0, score1)
            who = other(who) 
            print(dice())                                              
        else:
            score1 = take_turn(strategy1(score1, score0), score0, dice) + score1
            dice = select_dice(score1, score0)
            score1, score0 = swine_swap(score1, score0)
            who = other(who)
            print(dice())
        print(score0,score1)

    return score0, score1  # You may want to change this line.


#######################
# Phase 2: Strategies #
#######################

def always_roll(n):
    """Return a strategy that always rolls N dice.

    A strategy is a function that takes two total scores as arguments
    (the current player's score, and the opponent's score), and returns a
    number of dice that the current player will roll this turn.

    >>> strategy = always_roll(5)
    >>> strategy(0, 0)
    5
    >>> strategy(99, 99)
    5
    """
    def strategy(score, opponent_score):
        return n
    return strategy

# Experiments

def make_averaged(fn, num_samples=1000):
    """Return a function that returns the average_value of FN when called.

    To implement this function, you will have to use *args syntax, a new Python
    feature introduced in this project.  See the project description.

    >>> dice = make_test_dice(3, 1, 5, 6)
    >>> averaged_dice = make_averaged(dice, 1000)
    >>> averaged_dice()
    3.75
    >>> make_averaged(roll_dice, 1000)(2, dice)
    6.0

    In this last example, two different turn scenarios are averaged.
    - In the first, the player rolls a 3 then a 1, receiving a score of 1.
    - In the other, the player rolls a 5 and 6, scoring 11.
    Thus, the average value is 6.0.
    """
    def averaged_function(*args):
        counter = 0 
        Value = 0
        while counter < num_samples:
            Value = fn(*args) + Value 
            counter +=1
        return Value/num_samples
    return averaged_function




def max_scoring_num_rolls(dice=six_sided):
    """Return the number of dice (1 to 10) that gives the highest average turn
    score by calling roll_dice with the provided DICE.  Assume that dice always
    return positive outcomes.

    >>> dice = make_test_dice(3)
    >>> max_scoring_num_rolls(dice)
    10
    """
    
    rolling=1
    sum_numbers=0
    while rolling < 10:
        number = dice()        
        if number==1:
            return rolling
        else: 
            sum_numbers = sum_numbers+number 
        rolling += 1
    return rolling
    



def winner(strategy0, strategy1):
    """Return 0 if strategy0 wins against strategy1, and 1 otherwise."""
    score0, score1 = play(strategy0, strategy1)
    if score0 > score1:
        return 0
    else:
        return 1

def average_win_rate(strategy, baseline=always_roll(5)):
    """Return the average win rate (0 to 1) of STRATEGY against BASELINE."""
    win_rate_as_player_0 = 1 - make_averaged(winner)(strategy, baseline)
    win_rate_as_player_1 = make_averaged(winner)(baseline, strategy)
    return (win_rate_as_player_0 + win_rate_as_player_1) / 2 # Average results

def run_experiments():
    """Run a series of strategy experiments and report results."""
    if True: # Change to False when done finding max_scoring_num_rolls
        six_sided_max = max_scoring_num_rolls(six_sided)
        print('Max scoring num rolls for six-sided dice:', six_sided_max)
        four_sided_max = max_scoring_num_rolls(four_sided)
        print('Max scoring num rolls for four-sided dice:', four_sided_max)

    if True: # Change to True to test always_roll(8)
        print('always_roll(8) win rate:', average_win_rate(always_roll(8)))

    if True: # Change to True to test bacon_strategy
        print('bacon_strategy win rate:', average_win_rate(bacon_strategy))

    if True: # Change to True to test swap_strategy
        print('swap_strategy win rate:', average_win_rate(swap_strategy))

    if True: # Change to True to test final_strategy
        print('final_strategy win rate:', average_win_rate(final_strategy))

    "*** You may add additional experiments as you wish ***"

# Strategies

def bacon_strategy(score, opponent_score, margin=8, num_rolls=5):
    """This strategy rolls 0 dice if that gives at least MARGIN points,
    and rolls NUM_ROLLS otherwise.
    """
    if free_bacon(opponent_score) >=margin:
        return 0
    else:
        return num_rolls
    

def swap_strategy(score, opponent_score, margin=8, num_rolls=5):
    """This strategy rolls 0 dice when it would result in a beneficial swap and
    rolls NUM_ROLLS if it would result in a harmful swap. It also rolls
    0 dice if that gives at least MARGIN points and rolls
    NUM_ROLLS otherwise.
    """
    if 2*(score+free_bacon(opponent_score))==opponent_score:
        return 0
    elif score+free_bacon(opponent_score) == opponent_score*2:
        return num_rolls
    else:
        return bacon_strategy(score,opponent_score,margin,num_rolls)


        

    return None # Replace this statement

def leave_four_sided(score, opponent_score):
    if (free_bacon(opponent_score)+score+opponent_score)%7==0:
        return True
def trigger_swine(score,opponent_score):
    if 2*(score+1)==opponent_score:
        return 10
def prevent_trigger_swine(score,opponent_score):
    if score+1==2*opponent_score:
        return 1


def base_final_less_than(score, opponent_score, margin=4, num_rolls=5):
    """Modified copy of swap_strategy which adds leave_four_sided
    """
    if 2*(score+free_bacon(opponent_score))==opponent_score:
        return 0

    elif trigger_swine(score,opponent_score) is True:
        return 8

    else:
        return num_rolls

def base_final_greater_than(score, opponent_score, margin=6, num_rolls=5):
    """Modified copy of swap_strategy which adds leave_four_sided
    """
    if score+free_bacon(opponent_score) == opponent_score*2:
        if prevent_trigger_swine(score,opponent_score) is True:
            return 1
        else:
            return num_rolls
    elif prevent_trigger_swine(score,opponent_score) is True:
        return 1

    elif leave_four_sided(score,opponent_score) is True:
        return 0
    else:
        return num_rolls


def final_strategy(score, opponent_score,dice=six_sided):
    """Write a brief description of your final strategy.

    We first check if we can switch scores with my opponent with free bacon, because this will give
    me the biggest jump in scores. 


    If we have a lower score, then we return 0 into this strategy to 
    trigger the score switch. If we have a high score, then we stop considering other strategies where 
    rolling 0 might give us an advantage, instead returning max_scoring_num_rolls(dice). 

    If calling on free bacon 
    """
    if (score + opponent_score) % 7 == 0:
        if score > opponent_score:
            return base_final_greater_than(score,opponent_score,4,3)
        elif score == opponent_score:
            return 5
        else:
            return base_final_less_than(score,opponent_score,6,4)
    else:
        if score > opponent_score:
            return base_final_greater_than(score,opponent_score,5,4)
        elif score == opponent_score:
            return 5

        else:
            return base_final_less_than(score,opponent_score,8,6)
   


    


##########################
# Command Line Interface #
##########################

# Note: Functions in this section do not need to be changed.  They use features
#       of Python not yet covered in the course.


@main
def run(*args):
    """Read in the command-line argument and calls corresponding functions.

    This function uses Python syntax/techniques not yet covered in this course.
    """
    import argparse
    parser = argparse.ArgumentParser(description="Play Hog")
    parser.add_argument('--run_experiments', '-r', action='store_true',
                        help='Runs strategy experiments')
    args = parser.parse_args()

    if args.run_experiments:
        run_experiments()
