struct param {
	int arg1;
	int arg2;
};

program PROG {
	version VERS {
		string func0(void)    = 1;
		int    func1(string)  = 2;
		int    func2(int)     = 3;
		int    func3(param)   = 4;
		int    sclientes(int) = 5;
		int    specas(char)   = 6;
		int    sclientes(char)   = 7;
		int    solicitapeca(char,int) = 8;
		void   endclient(void)= 9;
	} = 1;
} = 0x30009999;
