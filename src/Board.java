import java.util.ArrayList;
import java.util.Arrays;

public class Board
{
	final int[][] blocks;
	final static int NULLVALUE = 0;

	public Board(int[][] blocks)
	{
		// construct a board from an N-by-N array of blocks

		this.blocks = copy(blocks);

	}

	private int[][] copy(int[][] blocks)
	{
		int[][] copy = new int[blocks.length][blocks.length];
		for (int i = 0; i < blocks.length; i++)
		{
			for (int j = 0; j < blocks.length; j++)
			{
				copy[i][j] = blocks[i][j];
			}
		}

		return copy;
	}

	// (where blocks[i][j] = block in row i, column j)
	public int size()
	{
		return blocks.length * blocks.length;
		// board size N
	}

	public int dimension()
	{
		return blocks.length;
	}

	public int hamming()
	{
		int total = 0;
		for (int i = 0; i < blocks.length; i++)
		{
			for (int j = 0; j < blocks.length; j++)
			{
				if (transform(i, j) != blocks[i][j] && blocks[i][j] != 0)
				{
					total++;
				}
			}
		}

		return total;
	}

	public int manhattan()
	{
		// |(i1 - i2)| + |(j1 - j2)|
		int expectedI = 0;
		int expectedJ = 0;
		int manhattan = 0;

		for (int i = 0; i < blocks.length; i++)
		{
			for (int j = 0; j < blocks.length; j++)
			{
				if (blocks[i][j] != transform(i, j) && blocks[i][j] != 0)
				{
					expectedI = (blocks[i][j] - 1) / blocks.length;
					expectedJ = (blocks[i][j] - 1) % blocks.length;
					manhattan += Math.abs(expectedI - i) + Math.abs(expectedJ - j);
				}
			}
		}
		return manhattan;
		// sum of Manhattan distances between blocks and goal
	}

	private int transform(int i, int j)
	{
		return (i * dimension()) + j + 1;
	}

	public boolean isGoal()
	{

		return hamming() == 0;
		// is this board the goal board?
	}

	public boolean isSolvable()
	{
		int inversions = 0;
		int row = 0;
		int[] singleArray = to1D(blocks);

		for (int i = 0; i < singleArray.length - 1; i++)
		{
			for (int j = i; j < singleArray.length; j++)
			{

				if (singleArray[i] > singleArray[j] && singleArray[j] != NULLVALUE)
				{

					inversions++;
				}

				if (singleArray[j] == NULLVALUE)
				{
					row = j / dimension();
				}

			}
		}

		if ((size() % 2) > 0)
		{
			return (inversions % 2) == 0;

		} else
		{

			return ((row + inversions) % 2) != 0;

		}

		// is this board solvable?
	}

	public boolean equals(Object y)
	{
		Board obj = (Board) y;

		if (y == this)
		{
			return true;
		}
		if (y == null || y.getClass() != this.getClass())
		{
			return false;
		}
		if (obj.size() != this.size())
		{
			return false;
		}
		if (obj.dimension() != this.dimension())
		{
			return false;
		}

		return checkEqual(this.blocks, obj.blocks);
		// does this board equal y?
	}

	private boolean checkEqual(int[][] arr1, int[][] arr2)
	{
		int n = 0;
		int k = 0;
		int[] singleArray = new int[size()];
		int[] otherArray = new int[size()];

		for (int i = 0; i < arr1.length; i++)
		{
			for (int j = 0; j < arr2.length; j++)
			{
				singleArray[n++] = arr1[i][j];
				otherArray[k++] = arr2[i][j];
			}
		}
		return Arrays.equals(singleArray, otherArray);

	}

	private int[] to1D(int[][] d2)
	{
		int n = 0;
		int[] singleArray = new int[size()];
		for (int i = 0; i < d2.length; i++)
		{
			for (int j = 0; j < d2.length; j++)
			{
				singleArray[n++] = d2[i][j];
			}
		}
		return singleArray;

	}

	public Iterable<Board> neighbors()
	{
		ArrayList<Board> list = new ArrayList<Board>();
		int[] location = blankPoint();

		boolean up = true, down = true, right = true, left = true;

		if (location[0] == 0)
		{
			up = false;
		}
		if (location[0] == dimension() - 1)
		{
			down = false;
		}
		if (location[1] == 0)
		{
			left = false;
		}
		if (location[1] == dimension() - 1)
		{
			right = false;
		}

		if (up)
		{
			list.add(new Board(move(location[0], location[1], location[0] - 1, location[1])));
		}
		if (down)
		{
			list.add(new Board(move(location[0], location[1], location[0] + 1, location[1])));
		}

		if (left)
		{
			list.add(new Board(move(location[0], location[1], location[0], location[1] - 1)));
		}

		if (right)
		{
			list.add(new Board(move(location[0], location[1], location[0], location[1] + 1)));
		}

		return list;
	}

	private int[] blankPoint()
	{
		int[] locations = new int[2];
		for (int i = 0; i < blocks.length; i++)
		{
			for (int j = 0; j < blocks.length; j++)
			{
				if (blocks[i][j] == NULLVALUE)
				{
					locations[0] = i;
					locations[1] = j;
				}
			}
		}
		return locations;
	}

	private int[][] move(int i, int j, int i1, int j1)
	{
		int temp;
		int[][] toMove = copy(blocks);
		temp = toMove[i][j];
		toMove[i][j] = toMove[i1][j1];
		toMove[i1][j1] = temp;

		return toMove;
	}

	public String toString()
	{

		String f = dimension() + "\n";
		int n = 0;

		for (int i = 0; i < blocks.length; i++)
		{
			for (int j = 0; j < blocks.length; j++)
			{
				f += blocks[i][j] + "\t";
			}
			f += "\n";
		}

		return f;
		// string representation of this board (in the output format specified below)
	}

	public static void main(String[] args)
	{
		// unit tests (not graded)

		int[][] a = { { 1, 2, 3, 4 }, { 5, 0, 6, 8 }, { 9, 10, 7, 11 }, { 13, 14, 15, 12 } };
		int[][] v = { { 1, 2, 3, 4 }, { 5, 0, 6, 8 }, { 9, 10, 7, 11 }, { 13, 14, 15, 12 } };
		int[][] x = { { 1, 2, 3 }, { 4, 5, 6 }, { 8, 7, 0 } };
		Board b = new Board(a);
		Board y = new Board(v);
		Board z = new Board(x);

		System.out.println(b.equals(y));
		System.out.println("out " + b.hamming());

		// System.out.println(5 % 4);

		for (Board i : b.neighbors())
		{
			System.out.println(i.toString() + "\n");
		}
		System.out.println(b.isSolvable());

	}
}