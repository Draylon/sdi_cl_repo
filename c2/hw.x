struct param {
	int arg1;
	int arg2;
};

struct peca_req {
	char id;
	int qt;
};

program PROG {
	version VERS {
		string func0(void)    = 1;
		int    func1(string)  = 2;
		int    func2(int)     = 3;
		int    func3(param)   = 4;
		int    sclientes(int) = 5;
		int    speca(char)   = 6;
		int    specas(int)   = 7;
		int    sclientes(int)   = 8;
		int    solicitapeca(param) = 9;
		void   endclient(void)= 10;
	} = 1;
} = 0x30009999;
