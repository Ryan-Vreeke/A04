import java.io.File;
import java.util.Iterator;

import edu.princeton.cs.algs4.MinPQ;

@SuppressWarnings("unused")
public class Solver
{
	private MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
	private final SearchNode initial;
	private SearchNode goal;

	private class SearchNode implements Comparable<SearchNode>
	{

		private final Board board;
		private final int moves;
		private int priority;
		private SearchNode previous;

		public SearchNode(Board board, int moves, SearchNode previous)
		{
			this.board = board;
			this.moves = moves;
			this.priority = moves + board.manhattan();
			this.previous = previous;

		}

		public SearchNode()
		{
			this.board = new Board(null);
			this.moves = 0;
			this.priority = 0;
			this.previous = null;
		}

		@Override
		public int compareTo(SearchNode s)
		{
			if (this.priority > s.priority)
			{
				return 1;
			} else if (this.priority < s.priority)
			{
				return -1;
			} else
				return 0;

		}

	}

	public Solver(Board initial)
	{
		// find a solution to the initial board (using the A* algorithm)
		if (initial == null)
			throw new java.lang.NullPointerException();
		if (initial.isSolvable())
		{

			this.initial = new SearchNode(initial, 0, null);

			System.out.println(initial.dimension());

			run();

		} else
		{
			throw new java.lang.IllegalArgumentException();
		}
	}

	private void run()
	{
		int moves = 1;
		SearchNode current = initial;

		while (!current.board.isGoal())
		{

			for (Board b : current.board.neighbors())
			{
				SearchNode s = new SearchNode(b, moves, current);
				if (s.board.equals(s.previous.board))
				{

				} else
				{
					pq.insert(s);
				}

			}
			current = pq.delMin();

		}

		goal = current;

	}

	private int priorityOf(int moves, Board board)
	{
		return moves + board.hamming();
	}

	public int moves()
	{
		return initial.board.manhattan();

	}

	public Iterable<Board> solution()
	{
		return new Iterable<Board>()
		{

			@Override
			public Iterator<Board> iterator()
			{

				return new Iterator<Board>()
				{
					SearchNode i = goal;

					@Override
					public boolean hasNext()
					{

						return i.previous != null;
					}

					@Override
					public Board next()
					{
						Board b;
						if (i.board.isGoal())
						{
							b = i.board;
						} else
						{
							b = i.previous.board;
						}

						i = i.previous;

						return b;

					}

				};
			};

		};
		// sequence of boards in a shortest solution
	}

	public static void main(String[] args)
	{
		
		// create initial board from file
		In in = new In(new File("src/Board.txt"));
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// check if puzzle is solvable; if so, solve it and output solution
		if (initial.isSolvable())
		{
			Solver solver = new Solver(initial);
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}

		// if not, report unsolvable
		else
		{
			StdOut.println("Unsolvable puzzle");
		}
	}
}