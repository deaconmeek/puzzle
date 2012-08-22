import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class LongPlay {
	public static boolean doTests=false;
	public static boolean is3D=true;
	public static boolean clipDuplicates=true,verifyDuplicates=false;
	public static Integer checkDuplicateMultiple=(verifyDuplicates?(is3D?24:8):null);

	public static void printPattern(String s,long l) {
		System.out.println(patternString(s,l));
	}
	public static String patternString(long l) {
		return(patternString("",l));
	}
	public static String patternString(String s,long l) {
		s+="\n";
		if (is3D) {
			for (int y=0;y<4;y++) {
				for (int z=0;z<4;z++) {
					for (int x=0;x<4;x++) s+=((l&(1L<<(z*16+y*4+x)))==0?"-":"*");
					s+="  ";
				}
				s+="\n";
			}
		}
		else {
			for (int y=0,bi=0;y<8;y++) {
				for (int x=0;x<8;x++,bi++) s+=((l&(1L<<bi))==0?"-":"*");
				s+="\n";
			}
		}
		return(s);
	}
	public static long patternFromArray(Boolean[][] map) {
		long ret=0;
		long bit=1;
		if (is3D) {
			for (int y=0;y<4;y++) {
				for (int x=0;x<4;x++,bit<<=1) {
					if ((map.length>y)&&(map[y].length>x)&&map[y][x]) {
						ret|=bit;
					}
				}
			}
		}
		else {
			for (int y=0;y<8;y++) {
				for (int x=0;x<8;x++,bit<<=1) {
					if ((map.length>y)&&(map[y].length>x)&&map[y][x]) {
						ret|=bit;
					}
				}
			}
		}
		printPattern("Base block",ret);
		return(ret);
	}

	public static long flip2D(long blk) {
		long ret=0,bit=1;
		for (int y=0;y<8;y++) {
			for (int x=0;x<8;x++,bit<<=1) {
				if ((blk&bit)!=0) ret|=(1L<<(8*y+7-x));
			}
		}
//		printPattern("flipped ",ret);
//		printPattern("shifted by "+min1(ret),ret>>>min1(ret));
		return(ret);
	}

	public static long rotate2D(long blk) {
		long ret=0,bit=1;
		for (int y=0;y<8;y++) {
			for (int x=0;x<8;x++,bit<<=1) {
				if ((blk&bit)!=0) ret|=(1L<<(8*x+7-y));
			}
		}
//		printPattern("rotated ",ret);
//		printPattern("shifted by "+min1(ret),ret>>>min1(ret));
		return(ret);
	}

	static void addToMap(Map<Long,Long> map,long blk) {
		printPattern("variation",blk);
		map.put(shiftToFirst1(blk), blk);
	}
	
	static Collection<Long> getRotations(long blk) {
		Map<Long,Long> ret=new HashMap<Long,Long>();
		if (is3D) {
			long blk2=rotate3DAroundX(blk);
			long blk3=rotate3DAroundY(blk);
			addToMap(ret,blk);
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=flip3DKeepZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));

			addToMap(ret,blk=blk2);
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=flip3DKeepZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));

			addToMap(ret,blk=blk3);
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=flip3DKeepZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
			addToMap(ret,blk=rotate3DAroundZ(blk));
		}
		else {
			addToMap(ret,blk);
			addToMap(ret,blk=rotate2D(blk));
			addToMap(ret,blk=rotate2D(blk));
			addToMap(ret,blk=rotate2D(blk));
			addToMap(ret,blk=flip2D(blk));
			addToMap(ret,blk=rotate2D(blk));
			addToMap(ret,blk=rotate2D(blk));
			addToMap(ret,blk=rotate2D(blk));
		}
		return(ret.values());
	}
	public static long flip3DKeepZ(long blk) {
		long ret=0,bit=1;
		for (int z=0;z<4;z++) {
			for (int y=0;y<4;y++) {
				for (int x=0;x<4;x++,bit<<=1) {
					if ((blk&bit)!=0) ret|=(1L<<(16*z+4*y+3-x));
				}
			}
		}
//		printPattern("flipped(keepz) ",ret);
		return(ret);
	}

	public static long rotate3DAroundZ(long blk) {
		long ret=0,bit=1;
		for (int z=0;z<4;z++) {
			for (int y=0;y<4;y++) {
				for (int x=0;x<4;x++,bit<<=1) {
					if ((blk&bit)!=0) ret|=(1L<<(16*z+4*x+3-y));
				}
			}
		}
//		printPattern("rotz ",ret);
		return(ret);
	}

	public static long rotate3DAroundX(long blk) {
		long ret=0,bit=1;
		for (int z=0;z<4;z++) {
			for (int y=0;y<4;y++) {
				for (int x=0;x<4;x++,bit<<=1) {
					if ((blk&bit)!=0) ret|=(1L<<(16*y+4*(3-z)+x));
				}
			}
		}
		
//		printPattern("rotx ",ret);
		return(ret);
	}

	public static long rotate3DAroundY(long blk) {
		long ret=0,bit=1;
		for (int z=0;z<4;z++) {
			for (int y=0;y<4;y++) {
				for (int x=0;x<4;x++,bit<<=1) {
					if ((blk&bit)!=0) ret|=(1L<<(16*x+4*y+3-z));
				}
			}
		}
//		printPattern("roty ",ret);
		return(ret);
	}

	public static long shiftToFirst1(long l) {
		return(l>>>min1(l));
	}

	
	
	public static boolean canShiftWithoutWrappingOrClipping(long block,int baseShift,int shift) {
		if (is3D) {
			int maxx=-1,maxy=-1,maxz=-1;
			int minx=1,miny=-1,minz=-1;
			for (int z=0,bi=0;z<4;z++) {
				for (int y=0;y<4;y++) {
					for (int x=0;x<4;x++,bi++) {
						if ((block&(1L<<bi))!=0) {
							if (maxx==-1) {
								minx=maxx=x;
								miny=maxy=y;
								minz=maxz=z;
							}
							else {
								minx=Math.min(minx, x);
								miny=Math.min(miny, y);
								minz=Math.min(minz, z);
								maxx=Math.max(maxx, x);
								maxy=Math.max(maxy, y);
								maxz=Math.max(maxz, z);
							}
						}
					}
				}
			}
			int bshiftx=(baseShift&3),bshifty=((baseShift>>2)&3),bshiftz=((baseShift>>4)&3);
			minx-=bshiftx;maxx-=bshiftx;
			miny-=bshifty;maxy-=bshifty;
			minz-=bshiftz;maxz-=bshiftz;
			int shiftx=(shift&3),shifty=((shift>>2)&3),shiftz=((shift>>4)&3);
			return(
					(shiftx+maxx<4)&&(shifty+maxy<4)&&(shiftz+maxz<4)&&
					(shiftx+minx>=0)&&(shifty+miny>=0)&&(shiftz+minz>=0)
					);
		}
		else {
			block>>>=baseShift;
			int topBiti=0;for (int bi=0;bi<64;bi++) if ((block&(1L<<bi))!=0) topBiti=bi;
			return(
					(shift+topBiti<64)&& // i.e. no clipping of end
					(!((((block<<shift)&0x0101010101010101L)!=0)&&  // i.e. not touching both side walls at the same time
						(((block<<shift)&0x8080808080808080L)!=0)))
				);
		}
	}
	
	
	
	Block[] blocks=stGrabBlocks();
	
	public static Block[] stGrabBlocks() {
		List<Piece> pieces=PieceFactory.puzzlePieces1();
		int btIndex=0;
		List<Block> blks=new ArrayList<Block>();
		for (Piece pc : pieces) {
			System.out.println("Block "+((char)(btIndex+'a')));
			long baseBlock=patternFromArray(pc.positions[0].pieceMap);
			Collection<Long> blocks=getRotations(baseBlock);
			for (long block : blocks) {
				blks.add(new Block(btIndex,block));
			}
			btIndex++;
		}
		return(blks.toArray(new Block[blks.size()]));
	}
	
	

	
	
	public static class Block {
		int blockTypeIndex;
		long blockPattern,blockPatternLeftAligned,availShifts;
		int baseShift;
		
		public Block(int blockType,long piece) {
			blockTypeIndex=blockType;
			baseShift=min1(blockPattern=piece);
			blockPatternLeftAligned=(blockPattern>>>baseShift);
			availShifts=0;
			for (int shift=0;shift<64;shift++) {
				if (canShiftWithoutWrappingOrClipping(blockPattern,baseShift,shift)) {
					availShifts|=(1L<<shift);
				}
			}
			//System.out.println(toString());
		}
		
		
		public String toString() {
			return("Block "+blockTypeIndex+"\n"+
					patternString(blockPattern)+
					"\nShifted:\n"+patternString(blockPatternLeftAligned)+
					"Shifts:\n"+patternString(availShifts));
		}
	}
	
	
	
	public static class Board {
		Board parent;
		long used,usedBlockMask;
		int addedBlockTypeIndex;
		int blockTypeAt0,blockTypeAtX,blockTypeAtY,blockTypeAtZ;
		
		public Board() {
			parent=null;
			used=usedBlockMask=0;
			addedBlockTypeIndex=blockTypeAt0=blockTypeAtX=blockTypeAtY=blockTypeAtZ=-1;
		}
		
		
		private Board(Board parent,Block block,int shift) {
			this.parent=parent;
			long blkp=(block.blockPatternLeftAligned<<shift);
			blockTypeAt0=((blkp&1)!=0?block.blockTypeIndex:parent.blockTypeAt0); 
			if (is3D) {
				blockTypeAtX=((blkp&8)!=0?block.blockTypeIndex:parent.blockTypeAtX); 
				blockTypeAtY=((blkp&0x1000)!=0?block.blockTypeIndex:parent.blockTypeAtY); 
				blockTypeAtZ=((blkp&0x0001000000000000L)!=0?block.blockTypeIndex:parent.blockTypeAtZ); 
			}
			else {
				blockTypeAtX=((blkp&0x80)!=0?block.blockTypeIndex:parent.blockTypeAtX); 
				blockTypeAtY=((blkp&0x0100000000000000L)!=0?block.blockTypeIndex:parent.blockTypeAtY); 
			}
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
			long blkp=(block.blockPatternLeftAligned<<shift);
			if (((block.availShifts&(1L<<shift))==0)|| // the block would clip or wrap
					((used&blkp)!=0)) {  // the block doesn't fit
				return(null);   // no go
			}
			if (clipDuplicates) {
				if (is3D) {
					if ((blkp&0x9009000000009008L)!=0) {// corners
						if ((blockTypeAt0!=-1)&&(block.blockTypeIndex<blockTypeAt0)) return(null);
					}
					if ((blkp&0x8)!=0) {
						if ((blockTypeAtY!=-1)&&(block.blockTypeIndex>blockTypeAtY)) return(null);
						if ((blockTypeAtZ!=-1)&&(block.blockTypeIndex>blockTypeAtZ)) return(null);
					}
					if ((blkp&0x1000)!=0) {
						if ((blockTypeAtX!=-1)&&(block.blockTypeIndex<blockTypeAtX)) return(null);
						if ((blockTypeAtZ!=-1)&&(block.blockTypeIndex>blockTypeAtZ)) return(null);
					}
					if ((blkp&0x0001000000000000L)!=0) {
						if ((blockTypeAtX!=-1)&&(block.blockTypeIndex<blockTypeAtX)) return(null);
						if ((blockTypeAtY!=-1)&&(block.blockTypeIndex<blockTypeAtY)) return(null);
					}
				}
				else {
					if ((blkp&0x8100000000000080L)!=0) {// corners
						if ((blockTypeAt0!=-1)&&(block.blockTypeIndex<blockTypeAt0)) return(null);
					}
					if ((blkp&0x80)!=0) {
						if ((blockTypeAtY!=-1)&&(block.blockTypeIndex>blockTypeAtY)) return(null);
					}
					if ((blkp&0x0100000000000000L)!=0) {
						if ((blockTypeAtX!=-1)&&(block.blockTypeIndex<blockTypeAtX)) return(null);
					}
				}
			}
			return(new Board(this,block,shift));  // new board
		}
		
		public char[][] toArray2D() {
			long parUsed=(parent==null?0:parent.used);
			char c=(char)(addedBlockTypeIndex+'A');
			char[][] ret;
			if (parent==null) {
				ret=new char[8][];
				for (int i=0;i<8;i++) {
					ret[i]=new char[]{' ',' ',' ',' ',' ',' ',' ',' '};
				}
			}
			else ret=parent.toArray2D();
			for (int y=0,i=0;y<8;y++) {
				for (int x=0;x<8;x++,i++) {
					if ((used&(~parUsed)&(1L<<i))!=0) ret[y][x]=c;
				}
			}
			return(ret);
		}
		public char[][][] toArray3D() {
			long parUsed=(parent==null?0:parent.used);
			char c=(char)(addedBlockTypeIndex+'A');
			char[][][] ret;
			if (parent==null) {
				ret=new char[4][][];
				for (int i=0;i<4;i++) {
					ret[i]=new char[4][];
					for (int j=0;j<4;j++) {
						ret[i][j]=new char[]{' ',' ',' ',' '};
					}
				}
			}
			else ret=parent.toArray3D();
			for (int z=0,bi=0;z<4;z++) {
				for (int y=0;y<4;y++) {
					for (int x=0;x<4;x++,bi++) {
						if ((used&(~parUsed)&(1L<<bi))!=0) ret[z][y][x]=c;
					}
				}
			}
			return(ret);
		}
		public String toString() {
			String ret="";
			if (is3D) {
				char[][][] map=toArray3D();
				for (int y=0;y<4;y++) {
					for (int z=0;z<4;z++) {
						for (int x=0;x<4;x++) ret+=""+map[z][y][x];
						ret+="  ";
					}
					ret+="\n";
				}
			}
			else {
				char[][] map=toArray2D();
				for (char[] ca : map) {
					for (char c : ca) ret+=""+c;
					ret+="\n";
				}
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
			if (checkDuplicateMultiple!=null) clipDuplicates=true;
			long start=System.currentTimeMillis();
			doDFS(new Board(),1.0/lp.blocks.length);
			long end=System.currentTimeMillis();

			System.out.println("\n\n"+resultString(11,lp.winningBoardList)+"Found "+lp.winningBoardList.size()+" winning boards in "+(0.001*(end-start))+" seconds\n");

			if (checkDuplicateMultiple!=null) {
				int num=lp.winningBoardList.size();
				lp=new LongPlay();
				clipDuplicates=false;
				
				start=System.currentTimeMillis();
				doDFS(new Board(),1.0/lp.blocks.length);
				end=System.currentTimeMillis();
	
				System.out.println("\n\n"+resultString(11,lp.winningBoardList)+"Found "+lp.winningBoardList.size()+" winning boards in "+(0.001*(end-start))+" seconds\n");
				System.out.println("With clipping on I found "+num+" boards, with "+(num*checkDuplicateMultiple==lp.winningBoardList.size()?"is":"isn't")+" the expected value of "+lp.winningBoardList.size()+" (clipping off) divided by "+checkDuplicateMultiple);
			}
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
		if (is3D) {
			for (Board bd : boards) {
				ret+=bd.toString()+"\n\n";
			}
		}
		else {
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
						min1_16bit[(int)(msk>>>16)&0xffff],
						16+Math.min(
								min1_16bit[(int)(msk>>>32)&0xffff],
								16+min1_16bit[(int)(msk>>>48)&0xffff]))));
	}
}
