import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LongPlay {
	public static boolean doTests=false;

	public static long patternFromArray(Boolean[][] map) {
		long ret=0;
		long bit=1;
		for (int y=0;y<8;y++) {
			for (int x=0;x<8;x++,bit<<=(ret==0?0:1)) {
				if ((map.length>y)&&(map[y].length>x)&&map[y][x]) {
					ret|=bit;
				}
			}
		}
		return(ret);
	}

	public static boolean canShiftWithoutWrappingOrClipping(long block,int shift) {
		int topBiti=0;for (int bi=0;bi<64;bi++) if ((block&(1L<<bi))!=0) topBiti=bi;
		return(
				(shift+topBiti<64)&& // i.e. no clipping of end
				(!((((block<<shift)&0x0101010101010101L)!=0)&&  // i.e. not touching both side wals at the same time
					(((block<<shift)&0x8080808080808080L)!=0)))
			);
	}
	
	
	
	Block[] blocks=stGrabBlocks();
	
	public static Block[] stGrabBlocks() {
		List<Piece> pieces=PieceFactory.puzzlePieces1();
		int btIndex=0;
		List<Block> blks=new ArrayList<Block>();
		for (Piece pc : pieces) {
			for (PieceMap map : pc.positions) {
				blks.add(new Block(btIndex,map.pieceMap));
			}
			btIndex++;
		}
		return(blks.toArray(new Block[blks.size()]));
	}
	
	

	
	
	public static class Block {
		int blockTypeIndex;
		long blockPattern,availShifts;
		
		public Block(int blockType,Boolean[][] piece) {
			blockTypeIndex=blockType;
			blockPattern=patternFromArray(piece);
			availShifts=0;
			for (int shift=0;shift<64;shift++) {
				if (canShiftWithoutWrappingOrClipping(blockPattern,shift)) {
					availShifts|=(1L<<shift);
				}
			}
			//System.out.println(toString());
		}
		
		
		public String toString() {
			String s="",shfts="";
			for (int y=0;y<8;y++) {
				for (int x=0;x<8;x++) {
					s+=((blockPattern&(1L<<(y*8+x)))==0?"-":"*");
					shfts+=((availShifts&(1L<<(y*8+x)))==0?"-":"*");
				}
				s+="\n";
				shfts+="\n";
			}
			return("Block "+blockTypeIndex+"\n"+s+"Shifts:\n"+shfts);
		}
	}
	
	
	
	public static class Board {
		Board parent;
		long used,usedBlockMask;
		int addedBlockTypeIndex;
		int blockTypeAtTL,blockTypeAtTR;
		
		public Board() {
			parent=null;
			used=usedBlockMask=0;
			addedBlockTypeIndex=-1;
		}
		
		
		private Board(Board parent,Block block,int shift) {
			this.parent=parent;
			long blkp=(block.blockPattern<<shift);
			blockTypeAtTL=((blkp&0x01)!=0?block.blockTypeIndex:parent.blockTypeAtTL);
			blockTypeAtTR=((blkp&0x80)!=0?block.blockTypeIndex:parent.blockTypeAtTR);
			used=blkp|parent.used;
			usedBlockMask=(1L<<block.blockTypeIndex)|parent.usedBlockMask;
			addedBlockTypeIndex=block.blockTypeIndex;
			
			//System.out.println(toString());
		}
		
		
		public Board placeBlock(Block block) {
			if ((usedBlockMask&(1L<<block.blockTypeIndex))!=0) {
				return(null); // i.e. block has been used by this board or parents
			}
			int shift=min0(used); // we want to fill the next available 0 in the board
														// note that all blocks have been set with bit0=1
			long blkp=(block.blockPattern<<shift);
			if (((block.availShifts&(1L<<shift))==0)|| // the block would clip or wrap
					((used&blkp)!=0)) {  // the block doesn't fit
				return(null);   // no go
			}
			// should be (TL>TR)&&(BR>TR)&&(BL>TR)&&(TL<BR)
			if ((blkp&0x80)!=0) { // detecting rotation step one
				if (block.blockTypeIndex>blockTypeAtTL) return(null);
			}
			else if ((blkp&0x8000000000000000L)!=0) { // detecting rotation step two
				if ((block.blockTypeIndex<blockTypeAtTL)||
						(block.blockTypeIndex<blockTypeAtTR)) return(null);
			}
			else if ((blkp&0x0100000000000000L)!=0) { // detecting rotation step three
				if (block.blockTypeIndex<blockTypeAtTR) return(null);
			}
			return(new Board(this,block,shift));  // new board
		}
		
		public char[][] toArray() {
			long parUsed=(parent==null?0:parent.used);
			char c=(char)(addedBlockTypeIndex+'A');
			char[][] ret;
			if (parent==null) {
				ret=new char[8][];
				for (int i=0;i<8;i++) {
					ret[i]=new char[]{' ',' ',' ',' ',' ',' ',' ',' '};
				}
			}
			else ret=parent.toArray();
			for (int y=0,i=0;y<8;y++) {
				for (int x=0;x<8;x++,i++) {
					if ((used&(~parUsed)&(1L<<i))!=0) ret[y][x]=c;
				}
			}
			return(ret);
		}
		public String toString() {
			char[][] map=toArray();
			String ret="";
			for (char[] ca : map) {
				for (char c : ca) ret+=""+c;
				ret+="\n";
			}
			return(ret);
		}
	}
	
	
	
	
	
	List<Board> winningBoardList=new ArrayList<Board>();
	
	
	
	
	
	public static class DFS implements Runnable {
		public LongPlay lp=new LongPlay();
		
		public double progress=0;
		
		public static void main(String[] args) {
			new DFS().run();
		}
		
		@Override
		public void run() {
			progress=0;
			long start=System.currentTimeMillis();
			doDFS(new Board(),1.0/lp.blocks.length);
			long end=System.currentTimeMillis();

			System.out.println("\n\n"+resultString(11,lp.winningBoardList)+"Found "+lp.winningBoardList.size()+" winning boards in "+(0.001*(end-start))+" seconds\n");
		}
		public void doDFS(Board bd,double addProgress) {
			if (addProgress<0.1) doDFSWProgress(bd,addProgress);
			else {
				for (Block blk : lp.blocks) {
					Board bd2=bd.placeBlock(blk);
					if (bd2!=null) {
						doDFS(bd2,addProgress/lp.blocks.length);
					}
					else progress+=addProgress;
				}
			}
		}
		public void doDFSWProgress(Board bd,double addProgress) {
			for (Block blk : lp.blocks) {
				Board bd2=bd.placeBlock(blk);
				if (bd2!=null) {
					doDFS(bd2);
				}
				progress+=addProgress;
				System.out.println("Progress: "+(progress*100)+"%");
			}
		}
		public void doDFS(Board bd) {
			for (Block blk : lp.blocks) {
				Board bd2=bd.placeBlock(blk);
				if (bd2!=null) {
					if (bd2.used==0xffffffffffffffffL) {
						lp.winningBoardList.add(bd2);
						System.out.println("Found winner #"+lp.winningBoardList.size());
					}
					else doDFS(bd2);
				}
			}
		}
	}
	
	
	
	public static String resultString(int width,List<Board> boards) {
		String ret="";
		String[] lines=new String[8];
		for (int i=0;i<8;i++) lines[i]="";
		for (int bdi=0;bdi<boards.size();) {
			for (int j=0;(j<width)&&(bdi<boards.size());j++,bdi++) {
				String[] bdLines=boards.get(bdi).toString().split("\n");
				for (int i=0;i<8;i++) lines[i]+=(lines[i].length()==0?"":"   ")+bdLines[i];
			}
			for (int i=0;i<8;i++) {
				ret+=lines[i]+"\n";
				lines[i]="";
			}
			ret+="\n";
		}
		return(ret);
	}
	
	public static short min1_16bit[]=new short[0x10000];
	static {
		for (int b=1,bi=0;bi<16;bi++,b<<=1) {
			for (int msk=b,b2=b<<1;msk<0x10000;msk+=b2) min1_16bit[msk]=(short)bi;
		}
		min1_16bit[0]=0x1000;
		
		if (doTests) {
			Random random=new Random();
			for (int i=0;i<1000;i++) {
				long l=random.nextLong();
				int bi;
				for (bi=0;bi<64;bi++) if ((l&(1L<<bi))!=0) break;
				if (bi<64?(min1(l)!=bi):(min1(l)<100)) {
					System.out.println("Min1 wrong ("+l+" should be "+bi+" not "+min1(l));
				}
				for (bi=0;bi<64;bi++) if ((l&(1L<<bi))==0) break;
				if (bi<64?(min0(l)!=bi):(min0(l)<100)) {
					System.out.println("Min0 wrong ("+l+" should be "+bi+" not "+min1(l));
				}
			}
		}
	}
	public static int min0(long msk) {
		return(min1(~msk));
	}
	public static int min1(long msk) {
		return(Math.min(
				min1_16bit[(int)msk&0xffff],
				16+Math.min(
						min1_16bit[(int)(msk>>16)&0xffff],
						16+Math.min(
								min1_16bit[(int)(msk>>32)&0xffff],
								16+min1_16bit[(int)(msk>>48)&0xffff]))));
	}
}
