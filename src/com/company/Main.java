package com.company;

import net.razorvine.pyro.*;

import java.util.Random;
import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class Main {

    public static void ia_vez(PyroProxy servidor, String comp_escolha, String humano_escolha) throws IOException, InterruptedException {

        int profundidade;
        boolean fim_jogo;
        int x, y;
        int comp = (int) servidor.call("comp");

        Object celulas = servidor.call("celulas_vazias");
        profundidade = ((List) celulas).size();
        fim_jogo = (boolean) servidor.call("fim_jogo");

        if (profundidade == 0 || fim_jogo) {
            return;
        }

        servidor.call("limpa_console");
        System.out.println("Vez do Computador " + comp_escolha);
        System.out.println((String) servidor.call("exibe_tabuleiro", comp_escolha, humano_escolha));

        if (profundidade == 9) {
            Random escolha = new Random();

            x = escolha.nextInt(3);
            y = escolha.nextInt(3);
        } else {

            List move = (List) servidor.call("minimax", profundidade, comp);

            x = (int) move.get(0);
            y = (int) move.get(1);
        }

        servidor.call("exec_movimento", x, y, comp);
        TimeUnit.SECONDS.sleep(2);
    }

    public static void humano_vez(PyroProxy servidor, String comp_escolha, String humano_escolha) throws IOException {
        int profundidade;
        boolean fim_jogo;
        int x, y;

        Object celulas = servidor.call("celulas_vazias");
        profundidade = ((List) celulas).size();
        fim_jogo = (boolean) servidor.call("fim_jogo");

        if (profundidade == 0 || fim_jogo) {
            return;
        }

        int movimento = -1;
        int[][] movimentos = {
                {0, 0}, {0, 1}, {0, 2},
                {1, 0}, {1, 1}, {1, 2},
                {2, 0}, {2, 1}, {2, 2}};

        servidor.call("limpa_console");
        System.out.println("Sua vez " + humano_escolha);
        System.out.println((String) servidor.call("exibe_tabuleiro", comp_escolha, humano_escolha));

        while (movimento < 1 || movimento > 9) {


            Scanner ler = new Scanner(System.in);
            boolean tenta_movimento;
            int humano = (int) servidor.call("hum");

            System.out.println("Use um numero (1...9): ");
            movimento = ler.nextInt();

            x = movimentos[movimento-1][0];
            y = movimentos[movimento-1][1];

            tenta_movimento = (boolean) servidor.call("exec_movimento", x, y, humano);

            if (tenta_movimento == false){
                System.out.println("Movimento Invalido");
                movimento = -1;
            }

        }
    }

    public static void main(String[] args) throws IOException {
        // write your code here
        NameServerProxy ns = null;
        PyroProxy servidor = null;
        try {
//            Conexão com o servidor
            ns = NameServerProxy.locateNS(null);

            servidor = new PyroProxy(ns.lookup("teste.jogo"));

            servidor.call("limpa_console");
            char humano_escolha = ' ';
            char comp_escolha = ' ';
            char primeiro = ' ';
            int comp = (int) servidor.call("comp");
            int hum = (int) servidor.call("hum");
            Scanner ler = new Scanner(System.in);

//            Definindo escolha do Jogador
            while (humano_escolha != 'O' && humano_escolha != 'X'){

                System.out.println(' ');
                System.out.println("Escolha X ou O: ");
                humano_escolha = ler.next().toUpperCase().charAt(0);

            }

//          Definindo escolha do Computador

            if (humano_escolha == 'X')
                comp_escolha = 'O';
            else {
                comp_escolha = 'X';
            }

//          Definindo se o Jogador começa primeiro
            servidor.call("limpa_console");

            while (primeiro != 'S' && primeiro != 'N'){

                System.out.println("Primeiro a iniciar? [s/n]: ");
                primeiro = ler.next().toUpperCase().charAt(0);
            }

//          Loop do jogo

            while (((List) servidor.call("celulas_vazias")).size() > 0 && !((boolean) servidor.call("fim_jogo"))){

                if (primeiro == 'N'){
                    ia_vez(servidor, String.valueOf(comp_escolha), String.valueOf(humano_escolha));
                    primeiro = ' ';
                }

                humano_vez(servidor, String.valueOf(comp_escolha), String.valueOf(humano_escolha));
                ia_vez(servidor, String.valueOf(comp_escolha), String.valueOf(humano_escolha));
            }

//            Mensagem Final do Jogo

            if ((boolean) servidor.call("vitoria", hum)){
                servidor.call("limpa_console");
                System.out.println((String) servidor.call("exibe_tabuleiro", String.valueOf(comp_escolha), String.valueOf(humano_escolha)));
                System.out.println("Você Venceu!!");

            }
            if ((boolean) servidor.call("vitoria", comp)){
                servidor.call("limpa_console");
                System.out.println((String) servidor.call("exibe_tabuleiro", String.valueOf(comp_escolha), String.valueOf(humano_escolha)));
                System.out.println("Você Perdeu!!");
            }
            else {
                servidor.call("limpa_console");
                System.out.println((String) servidor.call("exibe_tabuleiro", String.valueOf(comp_escolha), String.valueOf(humano_escolha)));
                System.out.println("Empate!!");
            }


        } catch (IOException e) {
            System.out.println("Caught remoteobject.call IOException: " + e.getMessage());
            ns.close();
            servidor.close();
            return;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}