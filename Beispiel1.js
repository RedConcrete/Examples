function(c, a) {
	
//----------------------no args---------------------------

	if(!a){
	return('\nPlease provide argument `Nt`:  t1{`Nt`:#s.`0NPC`.`2loc`} \nor \nt1{t:[#s.`0NPC`.`2loc`,#s.`0NPC`.`2loc2`,#s.`0NPC`.`2loc3`,#s.`0NPC`.`2loc4`]}')
	}
	
//----------------------variables--------------------------

	let c1 = ['unlock', 'open', 'release'],
	//s = slolution
	s = {},
	log="",
	// o = output
	o = a.t.call(s),
	name = c.caller,	
	//p = primenumber
	p=`  
   
     
       
           
             
                 
                   
                       
                             
                               
                                     
                                         
                                           
                                               
                                                     
                                                           
                                                             
                                                                   
                                                                       
                                                                         
                                                                               
                                                                                   
                                                                                         
                                                                                                 `.split`
`.map(o=>o.length),
	d=[0,1,2,3,4,5,6,7,8,9],
    k3ys=["6hh8xw","cmppiq","sa23uw","tvfkyq","uphlaw","vc2c7q","xwz7ja"],
	color=["red","purple","blue","cyan","green","lime","yellow","orange"],
	acc=["redconcrete","saveconcrete","com"] // WITHELIST
	
//--------------------withlist--------------------------------------------------		
					
		if(!acc.includes(c.caller)){
		if(!a.charge) return ("\nPay 5KGC for script usage with `Ncharge`:`Vtrue`")
		var x = #ms.accts.xfer_gc_to({ to:"redconcrete", amount:5000, memo:"t1 fee from :"+ c.caller})
			if(!x.ok) return ('\n`D....NOT ENOUGH MONEY....`' + JSON.stringify(x))
		}
//--------------------hardline required-----------------------------------------
			
			if (o.includes('hardline required')) {
				return("\n`D....HARDLINE REQUIRED....`")
			}		
	
			while(true){
						
//----------------------EZ_21---------------------------------------------------
			if(o.includes('`NEZ_21`')){
				for (let c of c1) {
				s.EZ_21 = c
				o = a.t.call(s)
				if (!o.includes('correct unlock command')) break
				}
			}
//----------------------EZ_40---------------------------------------------------
			else if(o.includes('`NEZ_40`')){

				for (let c of c1) {
				s.EZ_40 = c
				o = a.t.call(s)
				if (o.includes('ez_prime` is missing')) break
				}
				
				for (let c of p){ 
				s.ez_prime = c
				o = a.t.call(s)
				if (!o.includes('correct prime')) break
				}
			}
//----------------------EZ_35---------------------------------------------------	
			else if(o.includes('`NEZ_35`')){			
				for (let c of c1) {
				s.EZ_35=c
				o = a.t.call(s)
				if (!o.includes('correct unlock command')) break
				}
				
				for (let c of d){ 
				s.digit = c
				o = a.t.call(s)
				if (!o.includes('correct digit')) break
				}
			}
//----------------------c001----------------------------------------------------
			else if (o.includes('`Nc001`')) {
				for (let c of color) {
				s.c001=c
				s.color_digit = c.length
				o = a.t.call(s)
				if (!o.includes('correct color name')) break
				}
			}
//----------------------c002----------------------------------------------------
			else if(o.includes('`Nc002`')){			
				for (var i = 0; i < color.length; i++) {
                s.c002 = color[i]
                s.c002_complement = color[(i+4) % color.length]
                o = a.t.call(s)
                if (!o.includes('correct color name')) break
                }
			}
//----------------------c003----------------------------------------------------
			else if (o.includes('`Nc003`')) {
				for (var i = 0; i < color.length; i++) {
				s.c003 = color[i]
				s.c003_triad_1 = color[(i+3) % color.length];
				s.c003_triad_2 = color[(i+5) % color.length];
				o = a.t.call(s)
				if (!o.includes('correct color name')) break
				}
			}
//----------------------l0cket---------------------------------------------------
			else if (o.includes('`Nl0cket`')) {
				for (let c of k3ys) {
				s.l0cket = c
				o = a.t.call(s)
				if (!o.includes('correct security k3y')) break
				}
			}			
//-----------------------DATA_CHECK---------------------------------------------
			else if (o.includes('`NDATA_CHECK` ')) {
				return ("\n`NDATA_CHECK` not yet implemented :( you get your money back")
				var x = #fs.accts.xfer_gc_to_caller({amount:5000, memo:"t1: "+ c.caller + " DATA_CHECK "})
			}
			else if(o.includes('Connection terminated')) break
			
			else break
		}
		
  return o
}