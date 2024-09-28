import { mount } from '@vue/test-utils';
import HeaderComponent from '@/components/HeaderComponent.vue';
import { createStore } from 'vuex';
import { Quasar } from 'quasar';

describe('HeaderComponent.vue', () => {
  let store;

  beforeEach(() => {
    // Mock Vuex store
    store = createStore({
      state: {
        usuario: {
          role: 'ADMIN',  // Definindo o papel do usuário como ADMIN
        },
      },
    });
  });

  it('renders the component correctly', () => {
    const wrapper = mount(HeaderComponent, {
      global: {
        plugins: [Quasar, store],
      },
      props: {
        processosCriados: 5,
        processosEmProgresso: 3,
        processosFinalizados: 2,
        processosArquivados: 1,
        actualQuadroId: 1,  // Quadro ativo definido
      },
    });

    // Verifica se o título do header está correto
    expect(wrapper.find('q-toolbar-title').text()).toBe('LegalFlow');
    
    // Verifica se o botão de 'Novo Usuário' está presente
    expect(wrapper.find('button[aria-label="Novo Usuário"]').exists()).toBe(true);

    // Verifica se os valores dos badges estão corretos
    expect(wrapper.find('q-badge').text()).toContain('5'); // Criado
  });

  it('emits the correct events', async () => {
    const wrapper = mount(HeaderComponent, {
      global: {
        plugins: [Quasar, store],
      },
      props: {
        processosCriados: 5,
        processosEmProgresso: 3,
        processosFinalizados: 2,
        processosArquivados: 1,
        actualQuadroId: 1,
      },
    });

    // Simular clique no botão de abrir o modal de novo quadro
    await wrapper.find('button[aria-label="Novo Quadro"]').trigger('click');
    expect(wrapper.emitted('abrir-modal-novo-quadro')).toBeTruthy();

    // Simular clique no botão de abrir o modal de novo usuário
    await wrapper.find('button[aria-label="Novo Usuário"]').trigger('click');
    expect(wrapper.emitted('abrir-modal-novo-usuario')).toBeTruthy();
  });

  it('hides buttons if conditions are not met', () => {
    const wrapper = mount(HeaderComponent, {
      global: {
        plugins: [Quasar, store],
      },
      props: {
        processosCriados: 5,
        processosEmProgresso: 3,
        processosFinalizados: 2,
        processosArquivados: 1,
        actualQuadroId: null, // Quadro ID indefinido
      },
    });

    // Os botões não devem aparecer se actualQuadroId for null
    expect(wrapper.find('button[aria-label="Novo Usuário"]').exists()).toBe(false);
    expect(wrapper.find('button[aria-label="Novo Quadro"]').exists()).toBe(false);
  });
});
