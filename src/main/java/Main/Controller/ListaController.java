/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Controller;

import Main.Model.Manga;
import Main.Model.MangaList;
import Main.Model.MangaListPK;
import Main.Model.Users;
import Main.Repository.MangaListRepository;
import Main.Repository.MangaRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Classe responsável por controlar as requisições relacionadas a lista de mangas
 * do usuário.
 *
 * @author - GuiBatalhoti
 * @author - Gabriel Nozawa
 */
@Controller
public class ListaController {

    /**
     * Repositório de mangas.
     */
    @Autowired
    private MangaRepository mr;

    /**
     * Repositório de listas de mangas.
     */
    @Autowired
    private MangaListRepository mlr;

    /**
     * Método responsável por retornar a página de listagem de mangas.
     *
     * @param idManga id do manga a ser listado
     * @return ModelAndView
     */
    @GetMapping("/followManga/{idManga}")
    public String followManga(@PathVariable("idManga") int idManga) {
        MangaList mangaList = new MangaList();
        Manga manga = mr.findByIdManga(idManga);
        //associa o manga para a lista

        Object principal = SecurityContextHolder. getContext().getAuthentication().getPrincipal();
        //associa o manga ao usuario
        Users user = (Users) principal;
        MangaListPK mlPK = new MangaListPK(user.getIdUser(), idManga);
        mangaList.setMangaListPK(mlPK);
        mangaList.setUsers(user);
        mangaList.setManga(manga);
        mlr.save(mangaList);
        return "redirect:/lista";
    }

    /**
     * Método responsável por retornar a página de listagem de mangas.
     * @param idManga
     * @return ModelAndView
     */
    @GetMapping("/meusManga/{idManga}")
    public ModelAndView updateMangaList(@PathVariable("idManga") int idManga) {
        //busca a lista pelo id do usuario e do manga
        Object principal = SecurityContextHolder. getContext().getAuthentication().getPrincipal();
        Users user = (Users) principal;
        MangaListPK mlPK = new MangaListPK(user.getIdUser(), idManga);
        MangaList mangaList = mlr.findByMangaListPK(mlPK);      
        
        //recupera o manga que a lista referencia
        Manga manga = mr.findByIdManga(idManga);

        //cria um ModelAndView com o mangaList encontrado
        ModelAndView mv = new ModelAndView("mangaUpdate");
        mv.addObject("mangaList", mangaList);
        mv.addObject("thisManga", manga);
        
        return mv;
    }

    /**
     * Método responsável por retornar a página de listagem de mangas.
     * @param formData
     * @return
     */
    @RequestMapping(value="/updateMangaList", method=RequestMethod.POST, 
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateMangaList(@RequestBody MultiValueMap<String, String> formData) {
        try{
            int mangaId = Integer.valueOf(formData.get("manga").get(0));
            Object principal = SecurityContextHolder. getContext().getAuthentication().getPrincipal();
            Users user = (Users) principal;
            MangaListPK mlPK = new MangaListPK(user.getIdUser(), mangaId);

            MangaList mangaList = new MangaList(mlPK);

            Date due = new SimpleDateFormat("yyyy-MM-dd").parse(formData.get("due").get(0));
            mangaList.setDue(due);
            
            //recupera o manga e verifica se os valores condizem
            Manga manga = mr.findByIdManga(mangaId);
            int read = Integer.valueOf(formData.get("dueChapters").get(0));
            int maxChapters = manga.getNumChapter();
            
            MangaList old = mlr.getReferenceById(mlPK);
            if(old.getDueChapters() != null){
                read += old.getDueChapters();
            }
            if(maxChapters > read){
                mangaList.setDueChapters(read);
            }else{
                mangaList.setDueChapters(maxChapters);
            }
            mangaList.setDescription(formData.get("description").get(0));
            mlr.save(mangaList);
            return "redirect:/meusManga";
        }catch(Exception e){
            return "redirect:/meusManga";
        }
    }

    /**
     * Método responsável por retornar a página de listagem de mangas.
     * @return ModelAndView
     */
    @GetMapping("/meusManga")
    public ModelAndView myList() {
        //busca a lista pelo id do usuario
        Object principal = SecurityContextHolder. getContext().getAuthentication().getPrincipal();
        Users user = (Users) principal;
        List<MangaList> mangaListList = mlr.findByUsers(user);
        
        ModelAndView mv = new ModelAndView("meusManga");
        mv.addObject("mangaList", mangaListList);
        
        return mv;
    }
}
