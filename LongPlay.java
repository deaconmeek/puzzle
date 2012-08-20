import java.util.ArrayList;
import java.util.List;

public class LongPlay {
	public static class Block {
		int blockIndex,blockTypeIndex;
		long blockPattern,requiresPattern,availShifts;
		long myBlockMask,similarBlockMask;
		public Block(int index,int btIndex,long simlMask,Boolean[][] piece) {
			blockIndex=index;
			blockTypeIndex=btIndex;
			myBlockMask=(1L<<index);
			similarBlockMask=simlMask;
			blockPattern=0;
			long bit=1;
			for (int y=0;y<8;y++) {
				for (int x=0;x<8;x++,bit<<=(blockPattern==0?0:1)) {
					if ((piece.length>y)&&(piece[y].length>x)&&piece[y][x]) {
						blockPattern|=bit;
					}
				}
			}
			requiresPattern=0;
			availShifts=0;
			bit=1;
			long avaiBdMask=0xffffffffffffffffL;
			for (int shift=0;shift<8*8;avaiBdMask&=~(1L<<(63-shift)),shift++,bit<<=1) {
				if (((avaiBdMask&blockPattern)==blockPattern)&&
						!(
								(((blockPattern<<shift)&0x0101010101010101L)!=0)&&
								(((blockPattern<<shift)&0x8080808080808080L)!=0))
								) {
					availShifts|=bit;
				}
			}
			
			String simls="";
			for (int i=0;i<64;i++) if ((this.similarBlockMask&(1L<<i))!=0) simls+=""+i+",";
			
			String s="",shfts="";
			for (int y=0;y<8;y++) {
				for (int x=0;x<8;x++) {
					s+=((blockPattern&(1L<<(y*8+x)))==0?"-":"*");
					shfts+=((availShifts&(1L<<(y*8+x)))==0?"-":"*");
				}
				s+="\n";
				shfts+="\n";
			}
			System.out.println("Block "+blockIndex+"  (siml : "+simls+")\n"+s+"Shifts:\n"+shfts+"\n");
		}
	}
	
	
	
	
	public static class Board {
		int boardIndex,parentIndex;
		long used,usedBlockMask;
	}
	
	
	
	public static class SelfContainedBoard {
		int boardIndex;
		SelfContainedBoard parent;
		long used,usedBlockMask;
		int blockTypeIndex;
		
		public SelfContainedBoard(int copyIndex,Board[] boards) {
			Board copy=boards[copyIndex];
			used=copy.used;
			usedBlockMask=copy.usedBlockMask;
			blockTypeIndex=-1;
			parent=(copy.boardIndex<0?
					null:
						new SelfContainedBoard(copy.boardIndex,boards)
			);
		}

		public SelfContainedBoard() {
			parent=null;
			used=usedBlockMask=0;
			blockTypeIndex=-1;
		}
		private SelfContainedBoard(SelfContainedBoard parent,long used,long usedBlockMask,int btIndex) {
			this.parent=parent;
			this.blockTypeIndex=btIndex;
			this.used=used;
			this.usedBlockMask=usedBlockMask;
		}
		
		public SelfContainedBoard placeBlock(Block blk) {
			if ((usedBlockMask&blk.similarBlockMask)!=0) return(null);
			int shift=min1(~used);long bit=(1L<<shift);
			return (((blk.availShifts&bit)==0)||
					((used&(blk.blockPattern<<shift))!=0)||
					((used&(blk.requiresPattern<<shift))!=(blk.requiresPattern<<shift))?
							null:
								new SelfContainedBoard(this,used|(blk.blockPattern<<shift),usedBlockMask|blk.myBlockMask,blk.blockTypeIndex));
		}
		
		public char[][] toArray() {
			long parUsed=(parent==null?0:parent.used);
			char c=(char)(blockTypeIndex+'a');
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
			if ((map[0][0]>map[7][0])||(map[0][0]>map[7][7])||(map[0][0]>map[0][7])||(map[0][7]>map[7][0])) return("duplicate");
			for (char[] ca : map) {
				for (char c : ca) ret+=""+c;
				ret+="\n";
			}
			return(ret);
		}
	}
	
	Block[] blocks=stGrabBlocks();
	
	public static Block[] stGrabBlocks() {
		List<Piece> pieces=PieceFactory.puzzlePieces1();
		int index=0;
		int btIndex=0;
		List<Block> blks=new ArrayList<Block>();
		for (Piece pc : pieces) {
			long simlMask=((1L<<(index+pc.positions.length))-1)-((1L<<(index))-1);
			for (PieceMap map : pc.positions) {
				blks.add(new Block(index++,btIndex,simlMask,map.pieceMap));
			}
			btIndex++;
		}
		return(blks.toArray(new Block[blks.size()]));
	}
	
	
	List<SelfContainedBoard> winningBoardList=new ArrayList<SelfContainedBoard>();
	
	
	
	
	
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
			doDFS(new SelfContainedBoard(),1.0/lp.blocks.length);
			long end=System.currentTimeMillis();
			System.out.println("\n\nFound "+lp.winningBoardList.size()+" winning boards in "+(0.001*(end-start))+" seconds\n");
		}
		public void doDFS(SelfContainedBoard bd,double addProgress) {
			if (addProgress<0.1) doDFSWProgress(bd,addProgress);
			else {
				for (Block blk : lp.blocks) {
					SelfContainedBoard bd2=bd.placeBlock(blk);
					if (bd2!=null) {
						doDFS(bd2,addProgress/lp.blocks.length);
					}
					else progress+=addProgress;
				}
			}
		}
		public void doDFSWProgress(SelfContainedBoard bd,double addProgress) {
			for (Block blk : lp.blocks) {
				SelfContainedBoard bd2=bd.placeBlock(blk);
				if (bd2!=null) {
					doDFS(bd2);
				}
				progress+=addProgress;
				System.out.println("Progress: "+(progress*100)+"%");
			}
		}
		public void doDFS(SelfContainedBoard bd) {
			for (Block blk : lp.blocks) {
				SelfContainedBoard bd2=bd.placeBlock(blk);
				if (bd2!=null) {
					if (bd2.used==0xffffffffffffffffL) {
						String s=bd2.toString();
						if (!s.equals("duplicate")) {
							lp.winningBoardList.add(bd2);
							System.out.println("Found:\n"+s);
						}
					}
					else doDFS(bd2);
				}
			}
		}
	}
	
	
	
	public static short min1_16bit[]=new short[0x10000];
	static {
		for (int b=1,bi=0;bi<16;bi++,b<<=1) {
			for (int msk=b,b2=b<<1;msk<0x10000;msk+=b2) min1_16bit[msk]=(short)bi;
		}
		min1_16bit[0]=0x1000;
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
