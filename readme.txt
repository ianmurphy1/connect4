I implemented the Negamax algorithm with alpha - beta pruning the best I could and spent most of my time with it.

The main thing throwing me off was where to extract the best move from throughout the running of the negamax. I have therefore included 2 copies of the AI Class, one that takes the move from inside the negamax algorithm and then the other that took it from within the getMove() method once negamax had completed it's run through for valid moves.

Also causing me issues was on what to return when a winning state was found. I tried many variations of Integer.MAX_VALUE; but I wasn't able to run enough tests to come to a decisive answer about which yielded the most successful results.

With the focus on the AI a GUI wasn't feasible so a simple CLI version of the game was created.

In game if the AI is the 1st to move or there is no one else after taking column 3 the AI will choose it, if it's chosen then it will choose 2 or 4.

If it's possible can you send me on a proper implementation of negamax, i think I'm almost there but those areas i said were causing issues are the only things I think I don't get about the algorithm.